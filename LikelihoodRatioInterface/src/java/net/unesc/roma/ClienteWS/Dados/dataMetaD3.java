package net.unesc.roma.ClienteWS.Dados;

public class dataMetaD3 {

    public dataMetaD3(plotConfig plotConfig, data[] datas) {
        this.plotConfig = plotConfig;
        this.datas = datas;
    }
    
    public dataMetaD3() {

    }
    
    public plotConfig plotConfig;
    public data[] datas;
    public String[] descEstudos;
    
}
