set -x

curl -X POST -d '{"email": "invalid@me", "password": "123456"}' \
  -H 'content-type: application/json' localhost:3000/login


curl -X POST -d '{"email": "valid@me.com", "password": "123456"}' \
  -H 'content-type: application/json' localhost:3000/login
