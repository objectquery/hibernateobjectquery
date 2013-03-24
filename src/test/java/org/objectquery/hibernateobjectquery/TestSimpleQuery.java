package org.objectquery.hibernateobjectquery;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.objectquery.ObjectQuery;
import org.objectquery.generic.GenericObjectQuery;
import org.objectquery.generic.OrderType;
import org.objectquery.generic.ProjectionType;
import org.objectquery.hibernateobjectquery.domain.Home;
import org.objectquery.hibernateobjectquery.domain.Person;

public class TestSimpleQuery {

	@Test
	public void testBaseCondition() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");

		Assert.assertEquals("select A from org.objectquery.hibernateobjectquery.domain.Person A where A.name  =  :name", HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testDupliedPath() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");
		qp.eq(target.getName(), "tom3");

		Assert.assertEquals("select A from org.objectquery.hibernateobjectquery.domain.Person A where A.name  =  :name AND A.name  =  :name1", HibernateObjectQuery
				.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testDottedPath() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.eq(target.getDud().getName(), "tom3");

		Assert.assertEquals("select A from org.objectquery.hibernateobjectquery.domain.Person A where A.dog.name  =  :dog_name AND A.dud.name  =  :dud_name",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testProjection() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.eq(target.getDog().getName(), "tom");

		Assert.assertEquals("select A.name from org.objectquery.hibernateobjectquery.domain.Person A where A.dog.name  =  :dog_name", HibernateObjectQuery
				.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testProjectionCountThis() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target, ProjectionType.COUNT);
		qp.eq(target.getDog().getName(), "tom");

		Assert.assertEquals("select  COUNT(A) from org.objectquery.hibernateobjectquery.domain.Person A where A.dog.name  =  :dog_name", HibernateObjectQuery
				.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testSelectOrder() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName());

		Assert.assertEquals("select A from org.objectquery.hibernateobjectquery.domain.Person A where A.dog.name  =  :dog_name order by A.name", HibernateObjectQuery
				.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testOrderAsc() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName(), OrderType.ASC);

		Assert.assertEquals("select A from org.objectquery.hibernateobjectquery.domain.Person A where A.dog.name  =  :dog_name order by A.name ASC", HibernateObjectQuery
				.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testOrderDesc() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName(), OrderType.DESC);
		qp.order(target.getDog().getName(), OrderType.DESC);

		Assert.assertEquals("select A from org.objectquery.hibernateobjectquery.domain.Person A where A.dog.name  =  :dog_name order by A.name DESC,A.dog.name DESC",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testOrderGrouping() {

		GenericObjectQuery<Home> qp = new GenericObjectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.eq(target.getAddress(), "homeless");
		qp.order(qp.box(target.getPrice()), ProjectionType.COUNT, OrderType.ASC);

		Assert.assertEquals("select A from org.objectquery.hibernateobjectquery.domain.Home A where A.address  =  :address group by A  order by  COUNT(A.price) ASC",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testOrderGroupingPrj() {

		GenericObjectQuery<Home> qp = new GenericObjectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.COUNT);
		qp.order(qp.box(target.getPrice()), ProjectionType.COUNT, OrderType.ASC);

		Assert.assertEquals(
				"select A.address, COUNT(A.price) from org.objectquery.hibernateobjectquery.domain.Home A group by A.address order by  COUNT(A.price) ASC",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testAllSimpleConditions() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");
		qp.like(target.getName(), "tom");
		qp.notLike(target.getName(), "tom");
		qp.gt(target.getName(), "tom");
		qp.lt(target.getName(), "tom");
		qp.gtEq(target.getName(), "tom");
		qp.ltEq(target.getName(), "tom");
		qp.notEq(target.getName(), "tom");
		qp.likeNc(target.getName(), "tom");
		qp.notLikeNc(target.getName(), "tom");

		Assert.assertEquals(
				"select A from org.objectquery.hibernateobjectquery.domain.Person A where A.name  =  :name AND A.name  like  :name1 AND A.name not like :name2 AND A.name  >  :name3 AND A.name  <  :name4 AND A.name  >=  :name5 AND A.name  <=  :name6 AND A.name  <>  :name7 AND UPPER(A.name) like UPPER(:name8) AND UPPER(A.name) not like UPPER(:name9)",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testINCondition() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		List<String> pars = new ArrayList<String>();
		qp.in(target.getName(), pars);
		qp.notIn(target.getName(), pars);

		Assert.assertEquals("select A from org.objectquery.hibernateobjectquery.domain.Person A where A.name  in  (:name) AND A.name  not in  (:name1)",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testContainsCondition() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		Person p = new Person();
		qp.contains(target.getFriends(), p);
		qp.notContains(target.getFriends(), p);

		Assert.assertEquals(
				"select A from org.objectquery.hibernateobjectquery.domain.Person A where :friends  member of  A.friends AND :friends1  not member of  A.friends",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testProjectionGroup() {

		ObjectQuery<Home> qp = new GenericObjectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(target.getAddress());

		Assert.assertEquals("select A.address, MAX(A.price) from org.objectquery.hibernateobjectquery.domain.Home A group by A.address order by A.address",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}

	@Test
	public void testProjectionGroupHaving() {

		ObjectQuery<Home> qp = new GenericObjectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(target.getAddress());
		qp.having(qp.box(target.getPrice()), ProjectionType.MAX).eq(0D);

		Assert.assertEquals("select A.address, MAX(A.price) from org.objectquery.hibernateobjectquery.domain.Home A group by A.address having MAX(A.price) = :price order by A.address",
				HibernateObjectQuery.hqlGenerator(qp).getQuery());

	}
}
