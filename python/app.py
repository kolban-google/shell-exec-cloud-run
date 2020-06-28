import os
import subprocess
from flask import Flask, request, make_response

print("Started...")
app = Flask(__name__)

@app.route('/')
def func1():
    print("Running")
    return "Hello World"

@app.route('/exec', methods=['POST'])
def route_exec():
    command = request.data.decode('utf-8')
    try:
        completedProcess = subprocess.run("env", shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=10, universal_newlines=True)
        response = make_response(completedProcess.stdout, 200)
        response.mimetype = "text/plain"
        return response
    except subprocess.TimeoutExpired:
        response = make_response("Timedout", 400)
        response.mimetype = "text/plain"
        return response
    return "/exec"

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get('PORT', 8080)))