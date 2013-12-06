jpa-entitygraph-test
====================

Test for various JPA 2.1 entity graph issues.

Tests can be run with different providers, currently EclipseLink 2.5.1 and Hibernate 4.3.0.CR1:

- EclipseLink: mvn -DskipTests=false test
  If you want to run the tests within Eclipse, do not activate any maven profile ("eclipselink" ist active by default) and specify an agent specified on the command line:
  -javaagent:local/maven/repository/org/eclipse/persistence/org.eclipse.persistence.jpa/2.5.1/org.eclipse.persistence.jpa-2.5.1.jar
  
- Hibernate: mvn -DskipTests=false test -Phibernate
  If you want to run the tests within Eclipse, do activate the maven profile "hibernate".
  
  
Comments and questions are welcome: www.gedoplan.de, dirk.weil(at)gedoplan.de
