package com.example.android.safetyfirst;

public class Finding {
    private Long id;
    private Float height;
    private Integer age;
    private String address;
    private String complexion;

    @Override
    public String toString() {
        return "Finding{" +
                "id=" + id +
                ", height=" + height +
                ", age=" + age +
                ", address='" + address +
                ", complexion='"+complexion+'\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

        /*public void setAge(Integer age) {
            this.age = age;
        }
        */

        /*public void setAddress(String address) {
            this.address = address;
        }
        */

        /*public void setComplexion(String complexion) {
            this.complexion = complexion;
        }
        */

    public Finding(Long id, Float height, Integer age, String address,String complexion) {
        this.id = id;
        this.height = height;
        this.age = age;
        this.address = address;
        this.complexion=complexion;
    }
        /*public Finding(Float height, Integer age, String address) {

            this.height = height;
            this.age = age;
            this.address = address;
        }
        */

    public Long getId() {
        return id;
    }

    public Float getHeight() {
        return height;
    }

        /*public Integer getAge() {
            return age;
        }
        */

    public String getAddress() {
        return address;
    }

        /*public String getComplexion() {
            return complexion;
        }
        */

}