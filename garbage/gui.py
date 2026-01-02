from flask import Flask, render_template_string, request, redirect, url_for, flash
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

base_template = '''
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{% block title %}Product Store{% endblock %}</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }
        
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5em;
        }
        
        h2 {
            color: #000000;
            margin-bottom: 20px;
        }
        
        .nav {
            background: #000000;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 30px;
        }
        
        .nav a {
            color: white;
            text-decoration: none;
            margin-right: 20px;
            padding: 8px 15px;
            border-radius: 3px;
            transition: background 0.3s;
        }
        
        .nav a:hover {
            background: #333333;
        }
        
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 25px;
            margin-top: 20px;
        }
        
        .product-card {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            transition: transform 0.3s, box-shadow 0.3s;
            background: #f9f9f9;
        }
        
        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        
        .product-image {
            width: 100%;
            height: 200px;
            background: #ddd;
            border-radius: 5px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 15px;
            font-size: 3em;
            color: #999;
        }
        
        .product-name {
            font-size: 1.5em;
            color: #333;
            margin-bottom: 10px;
        }
        
        .product-price {
            font-size: 1.3em;
            color: #000000;
            font-weight: bold;
            margin-bottom: 15px;
        }
        
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background: #000000;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            transition: background 0.3s;
            margin-right: 10px;
        }
        
        .btn:hover {
            background: #333333;
        }
        
        .btn-danger {
            background: #dc3545;
        }
        
        .btn-danger:hover {
            background: #c82333;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: bold;
        }
        
        .form-group input,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
        }
        
        .form-group textarea {
            resize: vertical;
            min-height: 100px;
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .product-detail {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-top: 20px;
        }
        
        .product-detail-image {
            width: 100%;
            height: 400px;
            background: #ddd;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 5em;
            color: #999;
        }
        
        .product-info p {
            margin-bottom: 15px;
            line-height: 1.6;
        }
        
        .product-info strong {
            color: #000000;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="nav">
            <a href="{{ url_for('index') }}">Home</a>
            <a href="{{ url_for('view_products') }}">View Products</a>
            <a href="{{ url_for('add_product') }}">Add Product</a>
        </div>
        
        {% with messages = get_flashed_messages() %}
            {% if messages %}
                {% for message in messages %}
                    <div class="alert alert-success">{{ message }}</div>
                {% endfor %}
            {% endif %}
        {% endwith %}
        
        {% block content %}{% endblock %}
    </div>
</body>
</html>
'''

home_template = '''
{% extends "base.html" %}

{% block title %}Home - Product Store{% endblock %}

{% block content %}
    <h1>Welcome to Product Store</h1>
    <div style="text-align: center; padding: 50px;">
        <h2>Manage Your Products Easily</h2>
        <p style="font-size: 1.2em; color: #666; margin: 20px 0;">
            Browse our collection, add new products, or manage existing ones.
        </p>
        <a href="{{ url_for('view_products') }}" class="btn" style="font-size: 1.2em; padding: 15px 30px;">
            Browse Products
        </a>
    </div>
{% endblock %}
'''

products_template = '''
{% extends "base.html" %}

{% block title %}All Products{% endblock %}

{% block content %}
    <h1>Product Catalog</h1>
    <p style="text-align: center; color: #666; margin-bottom: 20px;">
        Total Products: {{ products|length }}
    </p>
    
    {% if products %}
        <div class="product-grid">
            {% for product in products %}
                <div class="product-card">
                    <div class="product-image">🖼️</div>
                    <div class="product-name">{{ product.name }}</div>
                    <div class="product-price">${{ "%.2f"|format(product.price) }}</div>
                    <a href="{{ url_for('product_details', product_id=product.id) }}" class="btn">View Details</a>
                    <a href="{{ url_for('delete_product', product_id=product.id) }}" 
                       class="btn btn-danger" 
                       onclick="return confirm('Are you sure you want to delete this product?');">
                        Delete
                    </a>
                </div>
            {% endfor %}
        </div>
    {% else %}
        <p style="text-align: center; color: #999; font-size: 1.2em; margin-top: 50px;">
            No products available. <a href="{{ url_for('add_product') }}">Add your first product</a>
        </p>
    {% endif %}
{% endblock %}
'''

details_template = '''
{% extends "base.html" %}

{% block title %}{{ product.name }} - Details{% endblock %}

{% block content %}
    <h1>Product Details</h1>
    
    {% if product %}
        <div class="product-detail">
            <div>
                <div class="product-detail-image">🖼️</div>
                <p style="text-align: center; color: #999; margin-top: 10px;">
                    Image: {{ product.image }}
                </p>
            </div>
            
            <div class="product-info">
                <h2>{{ product.name }}</h2>
                <p><strong>Price:</strong> ${{ "%.2f"|format(product.price) }}</p>
                <p><strong>Product ID:</strong> {{ product.id }}</p>
                <p><strong>Details:</strong></p>
                <p>{{ product.details }}</p>
                
                <div style="margin-top: 30px;">
                    <a href="{{ url_for('view_products') }}" class="btn">Back to Products</a>
                    <a href="{{ url_for('delete_product', product_id=product.id) }}" 
                       class="btn btn-danger"
                       onclick="return confirm('Are you sure you want to delete this product?');">
                        Delete Product
                    </a>
                </div>
            </div>
        </div>
    {% else %}
        <p style="text-align: center; color: #999;">Product not found.</p>
        <p style="text-align: center;">
            <a href="{{ url_for('view_products') }}" class="btn">Back to Products</a>
        </p>
    {% endif %}
{% endblock %}
'''

add_product_template = '''
{% extends "base.html" %}

{% block title %}Add New Product{% endblock %}

{% block content %}
    <h1>Add New Product</h1>
    
    <form method="POST" style="max-width: 600px; margin: 0 auto;">
        <div class="form-group">
            <label for="name">Product Name:</label>
            <input type="text" id="name" name="name" required>
        </div>
        
        <div class="form-group">
            <label for="price">Price ($):</label>
            <input type="number" id="price" name="price" step="0.01" min="0" required>
        </div>
        
        <div class="form-group">
            <label for="image">Image Filename:</label>
            <input type="text" id="image" name="image" placeholder="e.g., product.jpg" required>
        </div>
        
        <div class="form-group">
            <label for="details">Product Details:</label>
            <textarea id="details" name="details" required></textarea>
        </div>
        
        <div class="form-group">
            <button type="submit" class="btn">Add Product</button>
            <a href="{{ url_for('view_products') }}" class="btn" style="background: #6c757d;">Cancel</a>
        </div>
    </form>
{% endblock %}
'''

@app.route('/')
def index():
    return render_template_string(base_template.replace('{% block content %}{% endblock %}',
                                                        home_template.split('{% block content %}')[1].split('{% endblock %}')[0]))

@app.route('/products')
def view_products():
    template = base_template.replace('{% extends "base.html" %}', '').replace(
        '{% block content %}{% endblock %}',
        products_template.split('{% block content %}')[1].split('{% endblock %}')[0]
    )
    return render_template_string(template, products=products)

@app.route('/product/<int:product_id>')
def product_details(product_id):
    product = next((p for p in products if p['id'] == product_id), None)
    template = base_template.replace('{% extends "base.html" %}', '').replace(
        '{% block content %}{% endblock %}',
        details_template.split('{% block content %}')[1].split('{% endblock %}')[0]
    )
    return render_template_string(template, product=product)

@app.route('/add', methods=['GET', 'POST'])
def add_product():
    if request.method == 'POST':
        # Generate new product ID
        new_id = max([p['id'] for p in products]) + 1 if products else 1

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
        flash(f'Product "{new_product["name"]}" added successfully!')
        return redirect(url_for('view_products'))

    # GET request - show form
    template = base_template.replace('{% extends "base.html" %}', '').replace(
        '{% block content %}{% endblock %}',
        add_product_template.split('{% block content %}')[1].split('{% endblock %}')[0]
    )
    return render_template_string(template)

@app.route('/delete/<int:product_id>')
def delete_product(product_id):
    global products

    # Find product by ID
    product = next((p for p in products if p['id'] == product_id), None)

    if product:
        # Remove product from list
        products = [p for p in products if p['id'] != product_id]
        flash(f'Product "{product["name"]}" deleted successfully!')

    return redirect(url_for('view_products'))

if __name__ == "__main__":
    app.run(debug=True)