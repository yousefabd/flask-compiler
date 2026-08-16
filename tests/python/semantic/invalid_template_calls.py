from flask import render_template
template_name = "index.html"

def first():
    return render_template()

def second():
    return render_template(template_name)
