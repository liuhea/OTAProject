var http=require('http')
var server = http.createServer(function(req,res){
	res.end('hello server!')

}).listen(3001);

console.log('Server listening at port：3001');
