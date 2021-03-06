/*
 * Sonar AD Plugin
 * Copyright (C) 2012-2014 Jiji Sasidharan
 * http://programmingforliving.com/
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.pfl.sonar.plugins.ad;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Test;

/**
 * Test case for {@link ADServerEntry}
 * 
 * @author Jiji Sasidharan
 */
public class ADServerEntryTest {

	/**
	 * Scenario:
	 *   a) Check all the parameters of the constructor are set properly
	 */
	@Test
	public void testCreate() {
		ADServerEntry serverEntry = new ADServerEntry(1, 100, "ldaphost", 389);
		assertEquals("Priority is not set properly", serverEntry.getPriority(), 1);
		assertEquals("Weight is not set properly", serverEntry.getWeight(), 100);
		assertEquals("Host is not set properly", serverEntry.getHost(), "ldaphost");
		assertEquals("Port is not set properly", serverEntry.getPort(), 389);
	}

	/**
	 * Scenario:
	 *   a) Check if the getURL is generating ldap url properly
	 */
	@Test
	public void testGetUrl() {
		ADServerEntry serverEntry = new ADServerEntry(1, 100, "ldaphost", 389);
		assertEquals("getURL is not returning properly", serverEntry.getUrl(), "ldap://ldaphost:389");
	}

	/**
	 * Scenario:
	 *   a) Check compareTo for different server priority (lower values are preferred)
	 */
	@Test
	public void testCompareWithDifferentPriority() {
		ADServerEntry server1, server2;
		TreeSet<ADServerEntry> serverList = new TreeSet<ADServerEntry>();
		serverList.add(server1 = new ADServerEntry(2, 100, "ldaphost", 389));
		serverList.add(server2 = new ADServerEntry(1, 200, "ldaphost", 389));
		assertEquals("Priority compare failed - all items not added", serverList.size(), 2);
		
		Iterator<ADServerEntry> serverIt = serverList.iterator();
		assertEquals("Priority compare failed", serverIt.next(), server2);
		assertEquals("Priority compare failed", serverIt.next(), server1);
		
	}

	/**
	 * Scenario:
	 *   a) Check compareTo for same server priority, but diff weight (higher values preferred)
	 */
	@Test
	public void testCompareWithSamePriorityDifferentWeight() {
		ADServerEntry server1, server2;
		TreeSet<ADServerEntry> serverList = new TreeSet<ADServerEntry>();
		serverList.add(server1 = new ADServerEntry(1, 100, "ldaphost", 389));
		serverList.add(server2 = new ADServerEntry(1, 200, "ldaphost", 389));
		assertEquals("Weight compare failed - all items not added", serverList.size(), 2);
		
		Iterator<ADServerEntry> serverIt = serverList.iterator();
		assertEquals("Weight compare failed", serverIt.next(), server2);
		assertEquals("Weight compare failed", serverIt.next(), server1);
	}

	/**
	 * Scenario:
	 *   a) Check compareTo for same server priority, weight, but different host/port (sort based on url)
	 */
	@Test
	public void testCompareWithSamePriorityAndWeightDifferentHost() {
		ADServerEntry server1, server2;
		TreeSet<ADServerEntry> serverList = new TreeSet<ADServerEntry>();
		serverList.add(server1 = new ADServerEntry(1, 100, "2ldaphost", 389));
		serverList.add(server2 = new ADServerEntry(1, 100, "1ldaphost", 389));
		assertEquals("url compare failed - all items not added", serverList.size(), 2);
		
		Iterator<ADServerEntry> serverIt = serverList.iterator();
		assertEquals("url compare failed", serverIt.next(), server2);
		assertEquals("url compare failed", serverIt.next(), server1);
	}

	/**
	 * Scenario:
	 *   a) Check compareTo with same records
	 */
	@Test
	public void testCompareWithSamePriorityWeightHostAndPort() {
		ADServerEntry server;
		TreeSet<ADServerEntry> serverList = new TreeSet<ADServerEntry>();
		serverList.add(new ADServerEntry(1, 100, "ldaphost", 389));
		serverList.add(server = new ADServerEntry(1, 100, "ldaphost", 389));
		assertEquals("Same entries added twice", serverList.size(), 1);
		
		Iterator<ADServerEntry> serverIt = serverList.iterator();
		assertEquals("Same entries added twice", serverIt.next(), server);
	}

	/**
	 * Scenario:
	 *   a) Check equals with same values
	 */
	@Test
	public void testEqualsWithSameValues() {
		ADServerEntry server1 = new ADServerEntry(1, 100, "ldaphost", 389);
		ADServerEntry server2 = new ADServerEntry(1, 100, "ldaphost", 389);
		assertTrue("equals() failed.", server1.equals(server2));
	}

	/**
	 * Scenario:
	 *   a) Check equals with different priority values
	 */
	@Test
	public void testEqualsWithDifferentPriority() {
		ADServerEntry server1 = new ADServerEntry(1, 100, "ldaphost", 389);
		ADServerEntry server2 = new ADServerEntry(2, 100, "ldaphost", 389);
		assertFalse("equals() failed for diff priority.", server1.equals(server2));
	}

	/**
	 * Scenario:
	 *   a) Check equals with different weight values
	 */
	@Test
	public void testEqualsWithDifferentWeight() {
		ADServerEntry server1 = new ADServerEntry(1, 100, "ldaphost", 389);
		ADServerEntry server2 = new ADServerEntry(1, 200, "ldaphost", 389);
		assertFalse("equals() failed for diff weight.", server1.equals(server2));
	}

	/**
	 * Scenario:
	 *   a) Check equals with different host values
	 */
	@Test
	public void testEqualsWithDifferentHost() {
		ADServerEntry server1 = new ADServerEntry(1, 100, "ldaphost1", 389);
		ADServerEntry server2 = new ADServerEntry(1, 100, "ldaphost2", 389);
		assertFalse("equals() failed for host.", server1.equals(server2));
	}

	/**
	 * Scenario:
	 *   a) Check equals with different port values
	 */
	@Test
	public void testEqualsWithDifferentPort() {
		ADServerEntry server1 = new ADServerEntry(1, 100, "ldaphost1", 389);
		ADServerEntry server2 = new ADServerEntry(1, 100, "ldaphost1", 399);
		assertFalse("equals() failed for port.", server1.equals(server2));
	}

	/**
	 * Scenario:
	 *   a) Check hashCode with same values
	 */
	@Test
	public void testHashCodeWithSameData() {
		ADServerEntry server1 = new ADServerEntry(1, 100, "ldaphost1", 389);
		ADServerEntry server2 = new ADServerEntry(1, 100, "ldaphost1", 389);
		assertEquals("hashCode() for same values returned different.", server1.hashCode(), server2.hashCode());
	}

	/**
	 * Scenario:
	 *   a) Check hashCode with different values
	 */
	@Test
	public void testHashCodeWithDifferentData() {
		ADServerEntry server1 = new ADServerEntry(1, 100, "ldaphost1", 389);
		assertNotEquals("hashCode() failed for different priority.", server1.hashCode(), new ADServerEntry(2, 100, "ldaphost1", 389).hashCode());
		assertNotEquals("hashCode() failed for different weight.", server1.hashCode(), new ADServerEntry(1, 101, "ldaphost1", 389).hashCode());
		assertNotEquals("hashCode() failed for different host.", server1.hashCode(), new ADServerEntry(1, 100, "ldaphost2", 389).hashCode());
		assertNotEquals("hashCode() failed for different port.", server1.hashCode(), new ADServerEntry(1, 100, "ldaphost1", 489).hashCode());
	}
}
