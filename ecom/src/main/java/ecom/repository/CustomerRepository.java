package ecom.repository;

import ecom.domain.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CustomerRepository {
    private SessionFactory sessionFactory;

    public CustomerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Customer> list() {
        Session session = sessionFactory.getCurrentSession();
        TypedQuery<Customer> query = session.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.addresses", Customer.class);
        return query.getResultList();
    }



    public boolean create(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.save(customer);
        return true;
    }

    public Customer get(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Customer.class, id);
    }

    public boolean update(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.update(customer);
        return true;
    }

    public boolean delete(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(customer);
        return true;
    }
}
