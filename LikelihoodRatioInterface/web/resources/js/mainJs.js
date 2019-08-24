function saveSvg1() {
    
    var name = "Grafico_1.svg";
    svgEl = $("#svgimg1").attr('value');
    
    if(svgEl){
       
        var svgBlob = new Blob([svgEl], {type:"image/svg+xml;charset=utf-8"});
        var svgUrl = URL.createObjectURL(svgBlob);
        var downloadLink = document.createElement("a");
        downloadLink.href = svgUrl;
        downloadLink.download = name;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
    }    
}

function saveSvg2() {
    
    var name = "Grafico_2.svg";
    svgEl2 = $("#svgimg2").attr('value'); 
        
    if(svgEl2){
        
        var svgBlob = new Blob([svgEl2], {type:"image/svg+xml;charset=utf-8"});
        var svgUrl = URL.createObjectURL(svgBlob);
        var downloadLink = document.createElement("a");
        downloadLink.href = svgUrl;
        downloadLink.download = name;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
    }
}

function saveSvg3() {
    debugger;
    var name = "Grafico_3.svg";
    svgEl2 = $("#svgimg3").attr('value'); 
        
    if(svgEl2){
        
        var svgBlob = new Blob([svgEl2], {type:"image/svg+xml;charset=utf-8"});
        var svgUrl = URL.createObjectURL(svgBlob);
        var downloadLink = document.createElement("a");
        downloadLink.href = svgUrl;
        downloadLink.download = name;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
    }
}