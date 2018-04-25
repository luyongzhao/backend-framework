<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>
	<class name="${pkgName}.${table.className}" table="${table.name}">
	
	<#list table.columns as col>
		<#if col.code == "id">
		<id name="id" type="${col.type}">
			<column name="id" />
			<generator class="assigned" />
		</id>
		</#if>
	</#list>

	<#list table.columns as col>
		<#if col.code != "id">
		<property name="${col.code}" type="${col.type}">
			<column name="${col.code}" />
		</property>
		</#if>
	</#list>
	</class>
</hibernate-mapping>
