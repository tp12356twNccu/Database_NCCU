public class BarObject {
    private String name,district,style,openTime,closeTime;
	
	

    //TODO: add more based on DB if needed

    public BarObject(String name,String style,String district,String openTime,String closeTime) {
        this.name = name;
        this.district = district;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public String getDistrict() {
        return district;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    @Override
    public String toString() {
        return String.format("%-10s%-10s%-10d ~ %-5d%n", name,district,openTime,closeTime);
    }
    
    public String getStyle() {
    	return style;
    }
}