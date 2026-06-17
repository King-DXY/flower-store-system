#!/bin/bash
curl -X POST http://localhost:8080/api/auth/customer/register -H "Content-Type: application/json" -d "{\"name\":\"test\",\"phone\":\"13800138000\",\"email\":\"t@qq.com\",\"password\":\"123456\"}"
