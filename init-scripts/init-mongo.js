db = db.getSiblingDB('documents');
db.createCollection('raw');
db.createCollection('processed');
db.createCollection('report')