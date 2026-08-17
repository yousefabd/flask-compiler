condition = True

if condition:
    both = 1
else:
    both = 2

valid_both = both


if condition:
    maybe = 1

invalid_maybe = maybe


if condition:
    complete = 1
elif condition:
    complete = 2
else:
    complete = 3

valid_complete = complete


if condition:
    branch_copy = branch_later
    branch_later = 4


if condition:
    earlier_branch = 5
elif earlier_branch:
    pass