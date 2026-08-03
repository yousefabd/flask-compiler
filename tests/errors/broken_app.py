from flask import Flask, render_template

app = Flask(__name__)


@app.route('/')
def index():
    title = "Products"
    title = 42
    return render_template('index.html', page=missing_name, title=title)
