var result = [];
var table = $("#datagrid-row-r1-2--1").next().find('table');
if(table){
	var trs = table.find("tr");
	for(var i=0;i<trs.length;i++){
		var tr = trs[i];
		if(tr){
			var aEle = $(tr).find("a");
			var text = aEle.html();
			var href = aEle.attr('href');
			result.push({
				text:text,
				href:href
			});
		}
	}
}
console.log(result);
return result;
