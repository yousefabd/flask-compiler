from flask import Flask, render_template

app = Flask(__name__)

unit_price = 20
quantity = 3


@app.route('/literals')
def literals():
    total = unit_price * quantity
    return render_template(
        "page.html",
        name="Yousef",
        age=24,
        price=999.99,
        active=True,
        note=None,
        items=["a", "b"],
        data={"id": 1, "label": "x"},
        total=total
    )


@app.route('/product/<int:product_id>')
def product_details(product_id):
    return render_template("page.html", product=product_id)
