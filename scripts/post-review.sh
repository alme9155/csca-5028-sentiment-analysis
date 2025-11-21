#!/bin/bash

curl -X POST http://localhost:8881/analyze -H "Content-Type: application/x-www-form-urlencoded" --data-urlencode "text=I hated this terrible movie"
