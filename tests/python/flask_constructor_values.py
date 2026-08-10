from flask import Flask

app = Flask(__name__)

app.secret_key = "compiler-secret"

captured_module_name = __name__
captured_secret_key = app.secret_key