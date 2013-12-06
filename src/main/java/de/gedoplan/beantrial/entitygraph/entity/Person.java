package de.gedoplan.beantrial.entitygraph.entity;

import de.gedoplan.baselibs.persistence.entity.GeneratedIntegerIdEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity
@Access(AccessType.FIELD)
public class Person extends GeneratedIntegerIdEntity
{
  private static final long serialVersionUID = 1L;

  /**
   * Name.
   */
  private String            name;

  /**
   * Vorname.
   */
  private String            firstname;

  /**
   * (Privat-)Adresse.
   */
  private String            zipCode;
  private String            town;
  private String            street;

  @ElementCollection(fetch = FetchType.LAZY)
  @Column(name = "TEL")
  @OrderColumn
  private List<String>      phones;

  /**
   * Hobbies.
   */
  @ElementCollection(fetch = FetchType.LAZY)
  @Column(name = "HOBBY")
  @OrderColumn
  private List<String>      hobbies;

  /**
   * E-Mail-Adressen.
   */
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "PERSON_ID")
  private List<MailAddress> mailAddresses;

  public Person(String name, String firstname, MailAddress... mailAddress)
  {
    this.name = name;
    this.firstname = firstname;

    this.phones = new ArrayList<>();
    this.hobbies = new ArrayList<>();

    this.mailAddresses = new ArrayList<MailAddress>();
    for (MailAddress ma : mailAddress)
    {
      this.mailAddresses.add(ma);
    }
  }

  protected Person()
  {
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = interpretEmptyAsNull(name);
  }

  public String getFirstname()
  {
    return this.firstname;
  }

  public void setFirstname(String firstname)
  {
    this.firstname = interpretEmptyAsNull(firstname);
  }

  public String getZipCode()
  {
    return this.zipCode;
  }

  public void setZipCode(String zipCode)
  {
    this.zipCode = interpretEmptyAsNull(zipCode);
  }

  public String getTown()
  {
    return this.town;
  }

  public void setTown(String town)
  {
    this.town = interpretEmptyAsNull(town);
  }

  public String getStreet()
  {
    return this.street;
  }

  public void setStreet(String street)
  {
    this.street = interpretEmptyAsNull(street);
  }

  /**
   * Wert liefern: {@link #telefonNummern}.
   * 
   * @return Wert
   */
  public List<String> getPhones()
  {
    return this.phones;
  }

  /**
   * Wert liefern: {@link #hobbies}.
   * 
   * @return Wert
   */
  public List<String> getHobbies()
  {
    return this.hobbies;
  }

  /**
   * Mailadressen liefern.
   * 
   * @return Mailadressen
   */
  public List<MailAddress> getMailAddresses()
  {
    return this.mailAddresses;
  }

  /*
   * Workaround um Bug im GLF 4:javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL funktioniert nicht
   * TODO: Raus, wenn Bug gefixt
   */
  private static String interpretEmptyAsNull(String s)
  {
    return s == null || s.isEmpty() ? null : s;
  }

}
