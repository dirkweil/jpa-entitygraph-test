package de.gedoplan.beantrial.entitygraph.entity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.AttributeNode;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PublisherTest
{
  private static EntityManagerFactory entityManagerFactory;
  private static PersistenceUnitUtil  persistenceUnitUtil;
  private EntityManager               entityManager;

  @BeforeClass
  public static void beforeClass()
  {
    entityManagerFactory = Persistence.createEntityManagerFactory("default");

    persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();

    Publisher publisher = new Publisher("Expert Press", "Java");
    Book book = new Book("Entity Graphs Investigated", "12345678", 45);
    book.setPublisher(publisher);

    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    em.persist(publisher);
    em.getTransaction().commit();
    em.close();
  }

  @Before
  public void before()
  {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  @After
  public void after()
  {
    this.entityManager.close();
  }

  /**
   * Test: Ist der einfache EntityGraph Publisher.ENTITYGRAPH_BOOKS vorhanden und hat er die richtigen Attribute?
   */
  @Test
  public void testGetBasicEntityGraph()
  {
    EntityGraph<?> entityGraph = this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS);
    assertThat(entityGraph, is(not(nullValue())));
    assertThat(entityGraph, containsAttributes("books"));
  }

  /**
   * Test: Ist der komplexe EntityGraph Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS vorhanden und hat er die richtigen Attribute?
   */
  @Test
  public void testGetComplexEntityGraph()
  {
    EntityGraph<?> entityGraph = this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS);
    assertThat(entityGraph, is(not(nullValue())));
    assertThat(entityGraph, containsAttributes("books", "books.authors"));
  }

  /**
   * Test: Ist der komplexe EntityGraph Publisher.ENTITYGRAPH_BOOKS_AUTHORS_AND_NAMES vorhanden und hat er die richtigen
   * Attribute?
   */
  @Test
  public void testGetComplexEntityGraphExtended()
  {
    EntityGraph<?> entityGraph = this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AUTHORS_AND_NAMES);
    assertThat(entityGraph, is(not(nullValue())));
    assertThat(entityGraph, containsAttributes("books", "books.authors", "books.authors.name"));
  }

  /**
   * Test: Kann eine Query mit einem als String angegebenen Fetch Graph ausgeführt werden?
   */
  @Test
  public void testHintWithFetchGraphAsString()
  {
    runQuery("javax.persistence.fetchgraph", Publisher.ENTITYGRAPH_BOOKS);
  }

  /**
   * Test: Kann eine Query mit einem als Object angegebenen Fetch Graph ausgeführt werden?
   */
  @Test
  public void testHintWithFetchGraphAsObject()
  {
    runQuery("javax.persistence.fetchgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
  }

  /**
   * Test: Kann eine Query mit einem als String angegebenen Load Graph ausgeführt werden?
   */
  @Test
  public void testHintWithLoadGraphAsString()
  {
    runQuery("javax.persistence.loadgraph", Publisher.ENTITYGRAPH_BOOKS);
  }

  /**
   * Test: Kann eine Query mit einem als Object angegebenen Load Graph ausgeführt werden?
   */
  @Test
  public void testHintWithLoadGraphAsObject()
  {
    runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
  }

  /**
   * Test: Lädt eine Query mit einem einfachen Fetch Graph die id?
   */
  @Test
  public void testQueryWithBasicFetchGraphLoadsId()
  {
    List<Publisher> publishers = runQuery("javax.persistence.fetchgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
    createLoadStatusChecker().id(true).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem einfachen Fetch Graph die angeforderten Attribute?
   */
  @Test
  public void testQueryWithBasicFetchGraphLoadsGraph()
  {
    List<Publisher> publishers = runQuery("javax.persistence.fetchgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
    createLoadStatusChecker().books(true).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem einfachen Fetch Graph die anderen Attribute?
   */
  @Test
  public void testQueryWithBasicFetchGraphLoadsOther()
  {
    List<Publisher> publishers = runQuery("javax.persistence.fetchgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
    createLoadStatusChecker().categories(false).authors(false).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem einfachen Load Graph die id?
   */
  @Test
  public void testQueryWithBasicLoadGraphLoadsId()
  {
    List<Publisher> publishers = runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
    createLoadStatusChecker().id(true).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem einfachen Load Graph die angeforderten Attribute?
   */
  @Test
  public void testQueryWithBasicLoadGraphLoadsGraph()
  {
    List<Publisher> publishers = runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
    createLoadStatusChecker().books(true).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem einfachen Load Graph die anderen Attribute?
   */
  @Test
  public void testQueryWithBasicLoadGraphLoadsOther()
  {
    List<Publisher> publishers = runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
    createLoadStatusChecker().categories(true).authors(false).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem komplexen Fetch Graph die id?
   */
  @Test
  public void testQueryWithComplexFetchGraphLoadsId()
  {
    List<Publisher> publishers = runQuery("javax.persistence.fetchgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS));
    createLoadStatusChecker().id(true).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem komplexen Fetch Graph die angeforderten Attribute?
   */
  @Test
  public void testQueryWithComplexFetchGraphLoadsGraph()
  {
    List<Publisher> publishers = runQuery("javax.persistence.fetchgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS));
    createLoadStatusChecker().books(true).authors(true).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem komplexen Fetch Graph die anderen Attribute?
   */
  @Test
  public void testQueryWithComplexFetchGraphLoadsOther()
  {
    List<Publisher> publishers = runQuery("javax.persistence.fetchgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS));
    createLoadStatusChecker().categories(false).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem komplexen Load Graph die id?
   */
  @Test
  public void testQueryWithComplexLoadGraphLoadsId()
  {
    List<Publisher> publishers = runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS));
    createLoadStatusChecker().id(true).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem komplexen Load Graph die angeforderten Attribute?
   */
  @Test
  public void testQueryWithComplexLoadGraphLoadsGraph()
  {
    List<Publisher> publishers = runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS));
    createLoadStatusChecker().books(true).authors(true).check(publishers);
  }

  /**
   * Test: Lädt eine Query mit einem komplexen Load Graph die anderen Attribute?
   */
  @Test
  public void testQueryWithComplexLoadGraphLoadsOther()
  {
    List<Publisher> publishers = runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS));
    createLoadStatusChecker().categories(true).check(publishers);
  }

  //  /**
  //   * Test: Lädt eine Query mit einem einfachen Load Graph die richtigen Attribute?
  //   */
  //  @Test
  //  public void testSimpleLoadGraph()
  //  {
  //    List<Publisher> publishers = runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS));
  //    createLoadStatusChecker().books(true).categories(true).authors(false).check(publishers);
  //  }
  //
  //  /**
  //   * Test: Lädt eine Query mit einem komplexen Fetch Graph die richtigen Attribute?
  //   */
  //  @Test
  //  public void testComplexFetchGraph()
  //  {
  //    List<Publisher> publishers = runQuery("javax.persistence.fetchgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS));
  //    createLoadStatusChecker().books(true).categories(false).authors(true).check(publishers);
  //  }
  //
  //  /**
  //   * Test: Lädt eine Query mit einem komplexen Load Graph die richtigen Attribute?
  //   */
  //  @Test
  //  public void testComplexLoadGraph()
  //  {
  //    List<Publisher> publishers = runQuery("javax.persistence.loadgraph", this.entityManager.getEntityGraph(Publisher.ENTITYGRAPH_BOOKS_AND_AUTHORS));
  //    createLoadStatusChecker().books(true).categories(true).authors(true).check(publishers);
  //  }

  private List<Publisher> runQuery(String hintName, Object hintValue)
  {
    TypedQuery<Publisher> query = this.entityManager.createQuery("select p from Publisher p", Publisher.class);
    query.setHint(hintName, hintValue);
    return query.getResultList();
  }

  /**
   * Matcher für die Attributliste eines Entity Graph erstellen.
   * 
   * @param attributeNames Names der erwarteten Attribute
   * @return Matcher
   */
  private static Matcher<EntityGraph<?>> containsAttributes(String... attributeNames)
  {
    final Set<String> expected = new HashSet<>(Arrays.asList(attributeNames));

    Matcher<EntityGraph<?>> matcher = new TypeSafeDiagnosingMatcher<EntityGraph<?>>()
    {
      @Override
      public void describeTo(Description description)
      {
        description.appendValueList("Entity graph containing attributes ", ",  ", "", expected);
      }

      @Override
      protected boolean matchesSafely(EntityGraph<?> entityGraph, Description mismatchDescription)
      {
        Set<String> actual = getAttributeSet(entityGraph);
        if (actual.equals(expected))
        {
          return true;
        }

        mismatchDescription.appendValueList("containes ", ",  ", "", actual);
        return false;
      }
    };

    return matcher;
  }

  /**
   * Attributnamen eines Entity Graph liefern.
   * 
   * @param entityGraph Entity Graph
   * @return Set der Attributnamen
   */
  private static Set<String> getAttributeSet(EntityGraph<?> entityGraph)
  {
    Set<String> attributeSet = new HashSet<>();
    fillAttributeSet(entityGraph.getAttributeNodes(), attributeSet, "");
    return attributeSet;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static void fillAttributeSet(List<AttributeNode<?>> attributeNodes, Set<String> attributeSet, String prefix)
  {
    for (AttributeNode<?> attributeNode : attributeNodes)
    {
      String prefixedAttributeName = prefix + attributeNode.getAttributeName();
      attributeSet.add(prefixedAttributeName);
      for (Entry<Class, Subgraph> subGraphEntry : attributeNode.getSubgraphs().entrySet())
      {
        fillAttributeSet(subGraphEntry.getValue().getAttributeNodes(), attributeSet, prefixedAttributeName + ".");
      }

    }

  }

  //  @Test
  //  public void testSimpleFetchGraph()
  //  {
  //    System.out.println("----- testSimpleFetchGraph -----");
  //    testEntityGraph("Publisher_books", "javax.persistence.fetchgraph");
  //  }
  //
  //  @Test
  //  public void testSimpleLoadGraph()
  //  {
  //    System.out.println("----- testSimpleLoadGraph -----");
  //    testEntityGraph("Publisher_books", "javax.persistence.loadgraph");
  //  }
  //
  //  private void testEntityGraph(String graphName, String hintName)
  //  {
  //
  //    TypedQuery<Publisher> query = this.entityManager.createQuery("select p from Publisher p", Publisher.class);
  //
  //    EntityGraph<?> entityGraph = this.entityManager.getEntityGraph(graphName);
  //    Assert.assertNotNull("Entity Graph", entityGraph);
  //
  //    query.setHint(hintName, entityGraph);
  //
  //    System.out.println(hintName + " = " + graphName);
  //
  //    for (Publisher publisher : query.getResultList())
  //    {
  //      boolean booksLoaded = persistenceUnitUtil.isLoaded(publisher, "books");
  //      boolean categoriesLoaded = persistenceUnitUtil.isLoaded(publisher, "categories");
  //      System.out.println(publisher.toDebugString() + ", books loaded: " + booksLoaded + ", categories loaded: " + categoriesLoaded);
  //
  //      List<Book> books = publisher.getBooks();
  //      System.out.println("  books: " + books);
  //      if (books != null)
  //      {
  //        System.out.println("  #books: " + books.size());
  //        for (Book book : books)
  //        {
  //          System.out.println("  " + book.toDebugString());
  //        }
  //      }
  //
  //      List<String> categories = publisher.getCategories();
  //      System.out.println("  categories: " + categories);
  //      if (categories != null)
  //      {
  //        System.out.println("  #categories: " + categories.size());
  //        for (String category : categories)
  //        {
  //          System.out.println("  " + category);
  //        }
  //      }
  //    }
  //  }

  private static class LoadStatusChecker
  {
    private Boolean idLoadedExpected;
    private Boolean booksLoadedExpected;
    private Boolean categoriesLoadedExpected;
    private Boolean authorsLoadedExpected;

    public LoadStatusChecker id(boolean expected)
    {
      this.idLoadedExpected = expected;
      return this;
    }

    public LoadStatusChecker books(boolean expected)
    {
      this.booksLoadedExpected = expected;
      return this;
    }

    public LoadStatusChecker authors(boolean expected)
    {
      this.authorsLoadedExpected = expected;
      return this;
    }

    public LoadStatusChecker categories(boolean expected)
    {
      this.categoriesLoadedExpected = expected;
      return this;
    }

    public void check(List<Publisher> publishers)
    {
      assertThat(publishers, is(not(nullValue())));
      assertThat(publishers.size(), is(not(0)));

      Publisher publisher = publishers.get(0);

      if (this.idLoadedExpected != null)
      {
        assertThat("Publisher.id loaded", persistenceUnitUtil.isLoaded(publisher, "id"), is(this.idLoadedExpected));
      }

      boolean booksLoaded = persistenceUnitUtil.isLoaded(publisher, "books");
      if (this.booksLoadedExpected != null)
      {
        assertThat("Publisher.books loaded", booksLoaded, is(this.booksLoadedExpected));
      }

      if (booksLoaded && this.authorsLoadedExpected != null)
      {
        List<Book> books = publisher.getBooks();
        assertThat(books, is(not(nullValue())));
        assertThat(books.size(), is(not(0)));

        Book book = books.get(0);

        assertThat("Publisher.books.authors loaded", persistenceUnitUtil.isLoaded(book, "authors"), is(this.authorsLoadedExpected));
      }

      if (this.categoriesLoadedExpected != null)
      {
        assertThat("Publisher.categories loaded", persistenceUnitUtil.isLoaded(publisher, "categories"), is(this.categoriesLoadedExpected));
      }
    }

  }

  private static LoadStatusChecker createLoadStatusChecker()
  {
    return new LoadStatusChecker();
  }
}
