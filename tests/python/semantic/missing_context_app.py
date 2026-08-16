from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def index():
    return render_template(
        'missing_context_test.html',
        seed_value='seed',
        default_prefix='prefix',
        supplied_items=[]
    )
