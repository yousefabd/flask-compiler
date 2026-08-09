prefix = "Hello "

def greet(name):
    return prefix + name

def add(left, right):
    return left + right

def identity(value):
    return value

positional_message = greet("Yousef")
keyword_message = greet(name="Mona")
nested_message = greet(identity("Omar"))

total = add(20, 22)
nothing = identity(None)