boolean_sum = True + 1
boolean_unary = +True
boolean_order = True < 2
boolean_repeat = "x" * True
boolean_bits = True | False
list_order = [1] < [2]
set_order = {1} < {1, 2}
set_difference = {1, 2} - {2}
set_intersection = {1} & {2}
set_union = {1} | {2}
set_xor = {1} ^ {2}

rebound_function = 1
def rebound_function():
    return 1
function_result = rebound_function()

rebound_import = 1
from module import rebound_import
import_result = rebound_import()
