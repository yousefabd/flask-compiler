from flask import Flask, render_template

app = Flask(__name__)

products = [
    {'id': 1, 'name': 'Laptop', 'price': 999.99}
]


def find_product_by_id(product_id):
    for p in products:
        if p['id'] == product_id:
            return p
    return None


def total_price():
    global products
    total = 0
    for p in products:
        total += p['price']
    return total


@app.route('/')
def index():
    return render_template('index.html', page='home')


@app.route('/product/<int:product_id>')
def product_details(product_id):
    product = find_product_by_id(product_id)
    return render_template('index.html', page='details', product=product)
