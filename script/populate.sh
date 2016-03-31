curl -v -H "Content-Type: application/json" -X POST -d \
'{"transactionId":"1234567", "cardNo":"1234567890","channelId":"1000", "upc":"upc3", "loadValue":"110.00", "cardStatus":"ACT", "remark":"comments" }' \
http://192.168.99.100:8080/transaction


curl -v -H "Content-Type: application/json" -X GET http://192.168.99.100:8080/transaction?id=1234567

curl -v -H "Content-Type: application/json" -X DELETE http://192.168.99.100:8080/transaction?id=1234567


curl -v -H "Content-Type: application/json" -X POST -d \
'{"cardId":"53587ECBF0062E36FC63FE8580BF3ADB", "cardNo":"1234567890","cardStatus":"1", "cardUpc":"upc3", "assignedDp":"1000", "assignedStore":"100" }' \
http://127.0.0.1:8080/card



curl -v -H "Content-Type: application/json" -X GET http://127.0.0.1:8080/card?id=53587ECBF0062E36FC63FE8580BF3ADB

curl -v -H "Content-Type: application/json" -X DELETE http://127.0.0.1:8080/card?id=53587ECBF0062E36FC63FE8580BF3ADB



curl -v -H "Content-Type: application/json" -X POST -d \
'{"transactionId":"4D57DE7DE6F71C8F08FA08B26DC971F5", "cardId":"53587ECBF0062E36FC63FE8580BF3ADB", "type":"10","loadValue":"100.00", "channelId":"1200", "cardUpc":"upc3", "cardNo":"1234567890" }' \
http://127.0.0.1:8080/transaction



curl -v -H "Content-Type: application/json" -X POST -d \
'{"transactionId":"4D57DE7DE6F71C8F08FA08B26DC971F5", "type":"10","loadValue":"100.00", "channelId":"1200", "cardUpc":"upc3", "cardNo":"1234567890" }' \
http://127.0.0.1:8080/transaction

