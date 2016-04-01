


curl -v -H "Content-Type: application/json" -X POST -d \
'{"cardNo":"1234567890", "cardUpc":"upc1", "faceValue":"100.00", "cardType":"1", "cardStatus":"1", "assigneddp":"100", "assignedstore":"100" }' \
http://127.0.0.1:8080/card



curl -v -H "Content-Type: application/json" -X GET 'http://127.0.0.1:8080/card?cardNo=1234567890&cardUpc=upc1'

curl -v -H "Content-Type: application/json" -X DELETE 'http://127.0.0.1:8080/card?cardNo=1234567890&cardUpc=upc1'



curl -v -H "Content-Type: application/json" -X POST -d \
'{"transactionId":"4D57DE7DE6F71C8F08FA08B26DC971F5", "type":"10","loadValue":"100.00", "channelId":"1200", "cardUpc":"upc1", "cardNo":"1234567890" }' \
http://127.0.0.1:8080/transaction



curl -v -H "Content-Type: application/json" -X POST -d \
'{"transactionId":"4D57DE7DE6F71C8F08FA08B26DC971F5", "type":"10","loadValue":"100.00", "channelId":"1200", "cardUpc":"upc3", "cardNo":"1234567890" }' \
http://127.0.0.1:8080/transaction

