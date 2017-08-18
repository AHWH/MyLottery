/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.network.base;

public abstract class QueryParamBuilder {
    private String queryParam;

    public QueryParamBuilder(String queryParam) {
        this.queryParam = queryParam;
    }

    public abstract void buildParam(int drawNo);

    protected void addGrp1ParamModifier(int amt) {
        switch (amt) {
            case 0:
                queryParam += "0w";
                break;
            case 1:
                queryParam += "0x";
                break;
            case 2:
                queryParam += "0y";
                break;
            case 3:
                queryParam += "0z";
                break;
            case 4:
                queryParam += "00";
        }
    }

    protected void addGrp2ParamModifier(int amt) {
        switch (amt) {
            case 0:
                queryParam += "MD";
                break;
            case 1:
                queryParam += "MT";
                break;
            case 2:
                queryParam += "Mj";
                break;
            case 3:
                queryParam += "Mz";
                break;
            case 4:
                queryParam += "ND";
                break;
            case 5:
                queryParam += "NT";
                break;
            case 6:
                queryParam += "Nj";
                break;
            case 7:
                queryParam += "Nz";
                break;
            case 8:
                queryParam += "OD";
                break;
            case 9:
                queryParam += "OT";
        }
    }

    protected void addGrp3ParamModifier(int amt) {
        switch (amt) {
            case 0:
                queryParam += "A";
                break;
            case 1:
                queryParam += "E";
                break;
            case 2:
                queryParam += "I";
                break;
            case 3:
                queryParam += "M";
                break;
            case 4:
                queryParam += "Q";
                break;
            case 5:
                queryParam += "U";
                break;
            case 6:
                queryParam += "Y";
                break;
            case 7:
                queryParam += "c";
                break;
            case 8:
                queryParam += "g";
                break;
            case 9:
                queryParam += "k";
        }
    }

    protected void addGrp4ParamModifier(int amt) {
        switch (amt) {
            case 0:
                queryParam += "w";
                break;
            case 1:
                queryParam += "x";
                break;
            case 2:
                queryParam += "y";
                break;
            case 3:
                queryParam += "z";
                break;
            case 4:
                queryParam += "0";
                break;
            case 5:
                queryParam += "1";
                break;
            case 6:
                queryParam += "2";
                break;
            case 7:
                queryParam += "3";
                break;
            case 8:
                queryParam += "4";
                break;
            case 9:
                queryParam += "5";
        }
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    public String getQueryParam() {
        return queryParam;
    }
}
