package rs.skupstinans.util;

import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;

import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryDefinition;

import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.elementi.Clan;
import rs.skupstinans.propis.Propis;

public class Query {
	private String username;
	private int brojPropisa = -1;
	private boolean predlog;
	private boolean inProcedure;
	private boolean accepted;
	public final static int PROPIS = 0;
	public final static int AMANDMAN = 1;
	private int type = PROPIS;
	private boolean notUsvojen = true;
	private String text;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getBrojPropisa() {
		return brojPropisa;
	}

	public void setBrojPropisa(int brojPropisa) {
		this.brojPropisa = brojPropisa;
	}

	public boolean isPredlog() {
		return predlog;
	}

	public void setPredlog(boolean predlog) {
		this.predlog = predlog;
	}

	public boolean isInProcedure() {
		return inProcedure;
	}

	public void setInProcedure(boolean inProcedure) {
		this.inProcedure = inProcedure;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isNotUsvojen() {
		return notUsvojen;
	}

	public void setNotUsvojen(boolean notUsvojen) {
		this.notUsvojen = notUsvojen;
	}

	public StructuredQueryDefinition createQueryDefinition(StructuredQueryBuilder qb) {

		StructuredQueryDefinition queryDef = null;
		StructuredQueryDefinition boostQuery = null;
		String propisNamespace = Propis.class.getPackage().getAnnotation(XmlSchema.class).namespace();
		String clanNamespace = Clan.class.getPackage().getAnnotation(XmlSchema.class).namespace();
		String amandmaniNamespace = Amandmani.class.getPackage().getAnnotation(XmlSchema.class).namespace();

		if (username != null) {

			String namespace = propisNamespace;
			String elementName = "Propis";
			if (type == Query.AMANDMAN) {
				namespace = amandmaniNamespace;
				elementName = "Amandman";
			}
			boostQuery = qb.value(qb.elementAttribute(qb.element(new QName(namespace, elementName)),
					qb.attribute(new QName(clanNamespace, "usernameDonosioca"))), username);
			queryDef = boostQuery(queryDef, boostQuery, qb);
		}
		
		if (text != null) {
			String[] splits = text.split(" ");
			boostQuery = null;
			for (String split : splits) {
				split = split.trim();
				if (boostQuery != null) {
					boostQuery = qb.and(boostQuery, qb.term(split));
				}
				else {
					boostQuery = qb.term(split);
				}
			}
			queryDef = boostQuery(queryDef, boostQuery, qb);
		}

		if (brojPropisa != -1) {
			String namespace = propisNamespace;
			String elementName = "Propis";
			String attributeNamespace = propisNamespace;
			String attributeName = "brojPropisa";
			if (type == Query.AMANDMAN) {
				namespace = amandmaniNamespace;
				elementName = "Amandman";
				attributeNamespace = clanNamespace;
				attributeName = "references";
			}
			boostQuery = qb.value(qb.elementAttribute(qb.element(new QName(namespace, elementName)),
					qb.attribute(new QName(attributeNamespace, attributeName))), brojPropisa);
			queryDef = boostQuery(queryDef, boostQuery, qb);
		}

		if (type == Query.PROPIS) {
			StructuredQueryDefinition statusQuery = null;
			if (predlog) {
				statusQuery = qb.value(qb.elementAttribute(qb.element(new QName(propisNamespace, "Propis")),
						qb.attribute(new QName(propisNamespace, "status"))), "predlog");

			}

			if (inProcedure) {
				if (statusQuery != null) {
					statusQuery = qb.or(statusQuery,
							qb.value(qb.elementAttribute(qb.element(new QName(propisNamespace, "Propis")),
									qb.attribute(new QName(propisNamespace, "status"))), "usvojen u nacelu"),
							qb.value(
									qb.elementAttribute(qb.element(new QName(propisNamespace, "Propis")),
											qb.attribute(new QName(propisNamespace, "status"))),
									"usvojen u pojedinostima"));
				} else {
					statusQuery = qb.or(
							qb.value(qb.elementAttribute(qb.element(new QName(propisNamespace, "Propis")),
									qb.attribute(new QName(propisNamespace, "status"))), "usvojen u nacelu"),
							qb.value(
									qb.elementAttribute(qb.element(new QName(propisNamespace, "Propis")),
											qb.attribute(new QName(propisNamespace, "status"))),
									"usvojen u pojedinostima"));
				}
			}

			if (accepted) {

				if (statusQuery != null) {
					statusQuery = qb
							.or(statusQuery,
									qb.value(
											qb.elementAttribute(qb.element(new QName(propisNamespace, "Propis")),
													qb.attribute(new QName(propisNamespace, "status"))),
											"usvojen u celosti"));
				} else {
					statusQuery = qb.value(qb.elementAttribute(qb.element(new QName(propisNamespace, "Propis")),
							qb.attribute(new QName(propisNamespace, "status"))), "usvojen u celosti");
				}
			}

			if (statusQuery != null) {
				queryDef = boostQuery(queryDef, statusQuery, qb);
			}

		} else {
			if (notUsvojen) {
				boostQuery = qb.not(qb.value(qb.elementAttribute(qb.element(new QName(amandmaniNamespace, "Amandman")),
						qb.attribute(new QName(amandmaniNamespace, "usvojen"))), true));
				queryDef = boostQuery(queryDef, boostQuery, qb);
			}
		}
		return queryDef;
	}

	private StructuredQueryDefinition boostQuery(StructuredQueryDefinition orgDef, StructuredQueryDefinition boost,
			StructuredQueryBuilder builder) {
		if (boost != null) {
			if (orgDef != null) {
				orgDef = builder.and(orgDef, boost);
			} else {
				orgDef = boost;
			}
		}
		return orgDef;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
