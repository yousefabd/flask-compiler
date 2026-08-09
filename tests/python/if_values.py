selection = "unset"

if False:
    selection = "if"
elif 0:
    selection = "zero"
elif []:
    selection = "empty list"
elif "active":
    selection = "elif"
else:
    selection = "else"


fallback = "unset"

if None:
    fallback = "if"
else:
    fallback = "else"


def classify(value):
    if value > 0:
        return "positive"
    elif value < 0:
        return "negative"
    else:
        return "zero"


positive_result = classify(5)
negative_result = classify(-4)
zero_result = classify(0)


def check_access(active, age):
    if active:
        if age >= 18:
            return "allowed"

        return "minor"

    return "disabled"


adult_access = check_access(True, 24)
minor_access = check_access(True, 15)
disabled_access = check_access(False, 24)