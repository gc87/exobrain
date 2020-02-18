(ns exobrain.parse
  (:require [libpython-clj.python
             :refer [as-python as-jvm
                     ->python ->jvm
                     get-attr call-attr call-attr-kw
                     get-item att-type-map
                     call call-kw initialize!
                     as-numpy as-tensor ->numpy
                     run-simple-string
                     add-module module-dict
                     import-module
                     import-as
                     from-import
                     python-type]]
            [clojure.java.io :as io]
            [clucie.core :as core]
            [clucie.analysis :as analysis]
            [clucie.store :as store])
  (:import [org.jsoup Jsoup]
           [com.hankcs.hanlp HanLP]
           [com.hankcs.lucene HanLPTokenizer]))

(def python-executable (.getPath (io/file (io/resource "python-3.7.6-embed-amd64/python.exe") )) )
(def library-path (.getPath (io/file (io/resource "python-3.7.6-embed-amd64/python37.dll")))  )

(initialize! :python-executable python-executable
             :library-path library-path)

(def some-string "就像元数据是有关数据的数据一样，元编程就是编写用于操纵程序的某些程序。人们普遍认为，元程序就是生成其他程序的某些程序，但范式更加广泛。所有旨在自我读取、分析、转换或修改的程序都是元编程的范例。例如：\n\n领域特定语言 (DSL)\n解析器\n解释器\n编译器\n定理证明器\n术语重写器\n本教程探究 Python 中的元编程。本文通过考察 Python 的特性，更新您的 Python 知识，让您能够更深入地理解本教程中的概念。同时，还说明了 Python 中的类型为何会比只返回对象的类更重要。之后，讨论了在 Python 中进行元编程的方法，以及元编程如何简化某些任务。\n\n稍作反思\n如果您使用 Python 进行编程已经有段时间，可能知道一切都是对象，而类创建了这些对象。但是，如果一切都是对象（类也是对象），那么谁来创建这些类呢？这就是我要解答的问题。\n\n我们来验证一下上述说法是否正确：\n\n1\n2\n3\n4\n5\n>>> class SomeClass: \n...     pass\n>>> some_object = SomeClass()\n>>> type(some_obj)\n<__main__.SomeClass instance at 0x7f8de4432f80>\n因此，在对象上调用的 type 函数返回该对象的类。\n\n1\n2\n3\n4\n5\n6\n7\n>>> import inspect\n>>>inspect.isclass(SomeClass)\nTrue\n>>>inspect.isclass(some_object)\nFalse\n>>>inspect.isclass(type(some_object))\nTrue\n如果将类传递给 inspect.isclass，它就会返回 True，否则即返回 False。因为 some_object 不是类（它是 SomeClass 类的实例），所以 inspect.isclass 返回 False。又因为 type(some_object) 返回 some_object 的类，所以 inspect.isclass(type(some_object)) 返回 True：\n\n1\n2\n3\n4\n>>> type(SomeClass)\n<type 'classobj'>>>>\ninspect.isclass(type(SomeClass))\nTrue\n在 Python 3 中，所有类在默认情况下从 Classobj 类继承。现在，一切都说得通了。但 classobj 又该如何解释？我们来开开眼：\n\n1\n2\n3\n4\n5\n6\n7\n8\n>>> type(type(SomeClass))\n<type 'type'>\n>>>inspect.isclass(type(type(SomeClass)))\nTrue\n>>>type(type(type(SomeClass)))\n<type 'type'>\n>>>isclass(type(type(type(SomeClass))))\nTrue\n发现了吗？事实证明，一开始的说法（一切都是对象）并不完全正确。下面是更准确的说法：\n\n除 type 之外，Python 中的一切都是对象，它们要么是类的实例，要么是元类的实例。\n\n验证这一点：\n\n1\n2\n3\n4\n5\n>>> some_obj = SomeClass()\n>>> isinstance(some_obj,SomeClass)\nTrue\n>>> isinstance(SomeClass, type)\nTrue\n现在，我们知道了实例是实例化的类，而类是元类的实例。\n\ntype 并不是我们所想的那样\ntype 本身是类，也是自己的类型。它是一个元类。元类用于实例化并定义类的行为，就像类用于实例化并定义实例的行为一样。\n\ntype 是 Python 使用的内置元类。为改变 Python 中类的行为（比如，SomeClass 的行为），我们可以通过继承 type 元类，定义一个自定义元类。元类是在 Python 中进行元编程的一种方法。\n\n定义了某个类后会发生什么情况？\n我们首先回顾下已知的内容。Python 程序的基本构建块是：\n\n语句\n函数\n类\n语句用于在程序中执行实际的工作。语句可以在全局范围（模块级别）或局部范围（限于函数内）执行。函数类似代码的基本单元，由用于完成特定任务的一个或多个语句以某种顺序构成。可以在模块级别定义函数，也可以将函数定义为类的方法。类为函数提供面向对象的编程方法。它们定义对象如何进行实例化以及对象的特征（属性和方法）。\n\n类的名称空间被分层为不同的字典。例如：\n\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n>>> class SomeClass: \n...     class_var = 1\n...     def __init__(self): \n...         self.some_var = 'Some value'\n \n>>> SomeClass.__dict__\n{'__doc__': None,\n '__init__': <function __main__.__init__>,\n '__module__': '__main__',\n 'class_var': 1}\n \n>>> s = SomeClass()\n \n>>> s.__dict__\n{'some_var': 'Some value'}\n以下是每次遇到关键字类时发生的情况：\n\n类的主体（语句和函数）被隔离。\n创建类的名称空间字典（但尚未填充）。\n先执行类的主体，然后使用所有属性、定义的方法以及与类有关的其他一些有用信息来填充名称空间字典。\n在基类或要创建的类的元类挂钩（稍后解释）中确定元类。\n然后，通过类的名称、基类和属性调用元类，对其进行实例化。\n因为 type 是 Python 中的默认元类，所以可以在 Python 中使用 type 来创建类。\n\ntype 的另一面\n通过一个参数调用 type 时，会生成现有类的 type 信息。通过三个参数调用 type 时，会创建一个新的类对象。调用 type 时，参数是类名、基类列表以及指定类的名称空间的字典（所有字段和方法）。\n\n所以：\n>>> class SomeClass: pass\n\n等同于：\n>>> SomeClass = type('SomeClass', (), {})\n\n以及：\n\n1\n2\n3\n4\n5\n6\n7\nclass ParentClass: \n    pass\n \nclass SomeClass(ParentClass): \n    some_var = 5\n    def some_function(self): \n        print(\"Hello!\")\n实际等同于：\n\n1\n2\n3\n4\n5\n6\n7\n8\ndef some_function(self): \n    print(\"Hello\")\n \nParentClass = type('ParentClass', (), {})\nSomeClass = type('SomeClass',\n                 [ParentClass],\n                 {'some_function': some_function,\n                  'some_var':5})\n因此，通过使用自定义元类而不是 type，我们可以将某种行为注入到不太可能的类中。但是在实现元类来改变行为之前，我们来看一下在 Python 中进行元编程更为常见的方法。\n\n装饰器：在 Python 中进行元编程的常见例子\n装饰器是用于改变函数或类的行为的一种方法。装饰器的用法类似如下：")
;seg_list = jieba.cut("我来到北京清华大学", cut_all=True)
(def jieba (import-module "jieba"))
(def pseg (import-module "jieba.posseg"))

(call-attr jieba "enable_paddle")
(def seg-list (call-attr jieba "cut" "我来到北京清华大学" {:use_paddle true}))
(att-type-map seg-list)
(call-attr seg-list "__next__")

(def words (call-attr pseg "cut" some-string {:use_paddle true}))
(call-attr words "__next__")

(analysis/build-analyzer
  (HanLPTokenizer. (.. HanLP
                       (newSegment)
                       (enableOffset true)
                       (enableJapaneseNameRecognize true)) nil false))

;(def my-tokenizer (HanLPTokenizer. (.. HanLP
;                                       (newSegment)
;                                       (enableOffset true)
;                                       (enableJapaneseNameRecognize true)) nil false))

(def my-analyzer
  (analysis/build-analyzer
    (HanLPTokenizer. (.. HanLP
                         (newSegment)
                         (enableOffset true)
                         (enableJapaneseNameRecognize true)) nil false)))

(defn extract-text
  [is]
  (.text (Jsoup/parse is "UTF-8" "")))
