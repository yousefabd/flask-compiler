nested_visible = 1
valid_value = 0

def all_kinds(parameter_name, source):
    import os
    assigned_name = 1
    for loop_name in source:
        pass
    read_name
    object_name.attribute = 1
    global parameter_name, os, assigned_name, loop_name, read_name, object_name
    return read_name

def control_flow():
    if branch_condition:
        global branch_condition
    for item in iterable_name:
        global iterable_name
    while while_condition:
        global while_condition
        break

def outer():
    def nested_reader():
        return nested_visible
    global nested_visible
    return nested_reader

def outer_with_nested_violation():
    def inner():
        inner_missing
        global inner_missing
        return inner_missing
    return inner

def valid():
    global valid_value
    valid_value = 1
    return valid_value
