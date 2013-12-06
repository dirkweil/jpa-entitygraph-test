package de.gedoplan.beantrial.entitygraph.entity;

import de.gedoplan.baselibs.persistence.entity.GeneratedIntegerIdEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.FIELD)
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = Publisher.ENTITYGRAPH_BOOKS,
        attributeNodes = @NamedAttributeNode(value = "books"))
    ,
    @NamedEntityGraph(
        name = Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS,
        attributeNodes = @NamedAttributeNode(value = "books", subgraph = "Book.authors"),
        subgraphs = {
            @NamedSubgraph(
                name = "Book.authors",
                attributeNodes = @NamedAttributeNode(value = "authors")
            ),
        })
    ,
    @NamedEntityGraph(
        name = Publisher.ENTITYGRAPH_BOOKS_AUTHORS_AND_NAMES,
        attributeNodes = @NamedAttributeNode(value = "books", subgraph = "Book.authors"),
        subgraphs = {
            @NamedSubgraph(
                name = "Book.authors",
                attributeNodes = @NamedAttributeNode(value = "authors", subgraph = "Book.authors.name")
            ),
            @NamedSubgraph(
                name = "Book.authors.name",
                attributeNodes = @NamedAttributeNode("name")
            )
        })
})
public class Publisher extends GeneratedIntegerIdEntity
{
  private static final long  serialVersionUID                    = 1L;

  public static final String ENTITYGRAPH_BOOKS                   = "Publisher.books";
  public static final String ENTITYGRAPH_BOOKS_AND_AUTHORS       = "Publisher.booksAndAuthors";
  public static final String ENTITYGRAPH_BOOKS_AUTHORS_AND_NAMES = "Publisher.booksAuthorsAndName";

  private String             name;

  @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  private List<Book>         books;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String>       categories;

  protected Publisher()
  {
  }

  public Publisher(String name, String... categories)
  {
    this.name = name;

    this.books = new ArrayList<>();
    this.categories = new ArrayList<>(Arrays.asList(categories));
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public List<Book> getBooks()
  {
    return Collections.unmodifiableList(this.books);
  }

  void addBook(Book book)
  {
    this.books.add(book);
  }

  void removeBook(Book book)
  {
    this.books.remove(book);
  }

  public List<String> getCategories()
  {
    return this.categories;
  }
}
