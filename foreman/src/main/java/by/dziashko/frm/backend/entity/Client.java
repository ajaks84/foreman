package by.dziashko.frm.backend.entity;

//@Entity
public class Client extends AbstractEntity{
    private String name;

//    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
//    private List<Order> orders = new LinkedList<>();

//
    public Client (String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public List<Order> getOrders() {
//        return orders;
//    }
}

