from flask import Flask, render_template

app = Flask(__name__)


@app.route('/products')
def view_products():
    return render_template("products.html")


@app.route('/products')
def list_products():
    return render_template("products.html")
