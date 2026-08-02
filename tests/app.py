from flask import Flask, render_template, request, redirect, url_for, flash
import os

app = Flask(__name__)
app.secret_key = 'your_secret_key_here'

# Product database
products = [
    {
        'id': 1,
        'name': 'Laptop',
        'price': 999.99,
        'image': 'laptop.jpg',
        'details': 'High-performance laptop with 16GB RAM and 512GB SSD. Perfect for programming and multimedia tasks.'
    },
    {
        'id': 2,
        'name': 'Smartphone',
        'price': 699.99,
        'image': 'phone.jpg',
        'details': '5G enabled smartphone with 128GB storage and advanced camera system.'
    },
    {
        'id': 3,
        'name': 'Headphones',
        'price': 199.99,
        'image': 'headphones.jpg',
        'details': 'Wireless noise-canceling headphones with 30-hour battery life.'
    }
]


def find_product_by_id(product_id):
    """Find a product by its ID using a normal for loop."""
    for p in products:
        if p['id'] == product_id:
            return p
    return None


def get_max_product_id():
    """Get the maximum product ID using a normal for loop."""
    if not products:
        return 0
    
    max_id = 0
    for p in products:
        if p['id'] > max_id:
            max_id = p['id']
    return max_id


def remove_product_by_id(product_id):
    """Remove a product by its ID using a normal for loop."""
    global products
    new_products = []
    for p in products:
        if p['id'] != product_id:
            new_products.append(p)
    products = new_products

@app.route('/')
def index():
    return render_template('index.html', page='home')


@app.route('/products')
def view_products():
    return render_template('index.html', page='products', products=products)


@app.route('/product/<int:product_id>')
def product_details(product_id):
    product = find_product_by_id(product_id)
    return render_template('index.html', page='details', product=product)


@app.route('/add', methods=['GET', 'POST'])
def add_product():
    if request.method == 'POST':
        # Generate new product ID
        new_id = get_max_product_id() + 1

        # Create new product from form data
        new_product = {
            'id': new_id,
            'name': request.form['name'],
            'price': float(request.form['price']),
            'image': request.form['image'],
            'details': request.form['details']
        }

        # Add to products list
        products.append(new_product)
        message = 'Product "' + new_product["name"] + '" added successfully!'
        flash(message)
        return redirect(url_for('view_products'))

    # GET request - show form
    return render_template('index.html', page='add')


@app.route('/delete/<int:product_id>')
def delete_product(product_id):
    # Find product by ID
    product = find_product_by_id(product_id)

    if product:
        # Remove product from list
        remove_product_by_id(product_id)
        message = 'Product "' + product["name"] + '" deleted successfully!'
        flash(message)

    return redirect(url_for('view_products'))

@app.route('/render-test')
def render_test():
    user = {
        'name': 'Yousef',
        'age': 24,
        'status': {
            'active': True,
            'blocked': False,
            'pending': True
        },
        'optional_note': None
    }

    test_products = [
        {
            'name': 'Laptop',
            'price': 1000
        },
        {
            'name': 'Phone',
            'price': 700
        },
        {
            'name': 'Headphones',
            'price': 200
        },
        {
            'name': 'Gaming console',
            'price': 350
        }
    ]

    unit_price = 20
    quantity = 3
    calculated_total = unit_price * quantity

@app.route("/filter-test")
def filter_test():
    return render_template(
        "filter_test.html",
        items=["first", "second", "third"],
        data={
            "name": "Yousef",
            "age": 24
        },
        name="Yousef",
        price=999.99,
        quantity=3
    )

@app.route("/flash-test")
def flash_test():
    flash("Product saved successfully")
    flash("Check the product price", "warning")

    return render_template(
        "flash_test.html"
    )

@app.route("/url-test")
def url_test():
    return render_template(
        "url_test.html",
        product={
            "id": 2
        }
    )