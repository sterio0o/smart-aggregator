db = db.getSiblingDB('documents');
db.createCollection('raw');
db.createCollection('processed');
db.createCollection('report');

db.createUser({
    user: 'admin',
    pwd: 'root',
    roles: [{role: 'readWrite', db: 'documents'}]
});