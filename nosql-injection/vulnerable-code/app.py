from flask import Flask, request, jsonify
from pymongo import MongoClient 

app = Flask(__name__)
client = MongoClient("mongodb+srv://admin:abcdefg@ccsep-data-cluster.hqlqdmc.mongodb.net/?appName=CCSEP-data-Cluster")
db = client["test_db"]

@app.route("/login", methods=["POST"])
def login():
    username = str(request.json.get("username"))
    password = str(request.json.get("password"))
    user = db.users.find_one({"username": username, "password": password})
    if user:
        return jsonify({"message": "Login successful"}), 200
    return jsonify({"message": "Login failed"}), 401

if __name__ == "__main__":
    app.run(debug=True)