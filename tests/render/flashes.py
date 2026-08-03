from flask import Flask, render_template, flash

app = Flask(__name__)


@app.route('/saved')
def saved():
    flash("Product saved")
    flash("Check the price", "warning")
    return render_template("page.html")


@app.route('/maybe')
def maybe():
    if True:
        flash("Only sometimes")
    return render_template("page.html")
