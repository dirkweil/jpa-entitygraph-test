package de.gedoplan.beantrial.entitygraph.entity;

import de.gedoplan.baselibs.persistence.entity.GeneratedIntegerIdEntity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@Access(AccessType.FIELD)
public class Book extends GeneratedIntegerIdEntity
{
  private static final long serialVersionUID = 1L;

  private String            name;
  private String            isbn;
  private int               pages;

  @ManyToOne
  private Publisher         publisher;

  @ManyToMany
  private Set<Person>       authors;

  protected Book()
  {
  }

  public Book(String name, String isbn, int pages)
  {
    this.name = name;
    this.isbn = isbn;
    this.pages = pages;

    this.authors = new HashSet<>();
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getIsbn()
  {
    return this.isbn;
  }

  public void setIsbn(String isbn)
  {
    this.isbn = isbn;
  }

  public int getPages()
  {
    return this.pages;
  }

  public void setPages(int pages)
  {
    this.pages = pages;
  }

  public Publisher getPublisher()
  {
    return this.publisher;
  }

  public void setPublisher(Publisher publisher)
  {
    if (this.publisher != null)
    {
      this.publisher.removeBook(this);
    }

    this.publisher = publisher;

    if (this.publisher != null)
    {
      this.publisher.addBook(this);
    }
  }

  public Set<Person> getAuthors()
  {
    return this.authors;
  }
}
