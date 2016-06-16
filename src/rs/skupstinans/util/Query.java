package rs.skupstinans.util;

import java.util.Date;

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

	private String naziv;
	private String brojGlasila;
	private String status;
	private Date datumAcceptedOd;
	private Date datumAcceptedDo;
	private Date datumReleasedOd;
	private Date datumReleasedDo;
	private Date datumVazenjaOd;
	private Date datumVazenjaDo;
	private Date datumPrimenaOd;
	private Date datumPrimenaDo;
	private Date datumStupanjeOd;
	private Date datumStupanjeDo;
	private Date datumPredlogOd;
	private Date datumPredlogDo;
	
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


		if (naziv != null && !naziv.equals("")) {
			String namespace = clanNamespace;
			String elementName = "Naziv";
			String[] splits = naziv.split(" ");
			boostQuery = null;
			for (String split : splits) {
				split = split.trim();
				if (boostQuery != null) {
					boostQuery = qb.and(qb.word(qb.element(new QName(namespace, elementName)), split));
				}
				else {
					boostQuery = qb.word(qb.element(new QName(namespace, elementName)), split);
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
				System.out.println("IN PROCEDURE");
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

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getBrojGlasila() {
		return brojGlasila;
	}

	public void setBrojGlasila(String brojGlasila) {
		this.brojGlasila = brojGlasila;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDatumAcceptedOd() {
		return datumAcceptedOd;
	}

	public void setDatumAcceptedOd(Date datumAcceptedOd) {
		this.datumAcceptedOd = datumAcceptedOd;
	}

	public Date getDatumAcceptedDo() {
		return datumAcceptedDo;
	}

	public void setDatumAcceptedDo(Date datumAcceptedDo) {
		this.datumAcceptedDo = datumAcceptedDo;
	}

	public Date getDatumReleasedOd() {
		return datumReleasedOd;
	}

	public void setDatumReleasedOd(Date datumReleasedOd) {
		this.datumReleasedOd = datumReleasedOd;
	}

	public Date getDatumReleasedDo() {
		return datumReleasedDo;
	}

	public void setDatumReleasedDo(Date datumReleasedDo) {
		this.datumReleasedDo = datumReleasedDo;
	}

	public Date getDatumVazenjaOd() {
		return datumVazenjaOd;
	}

	public void setDatumVazenjaOd(Date datumVazenjaOd) {
		this.datumVazenjaOd = datumVazenjaOd;
	}

	public Date getDatumVazenjaDo() {
		return datumVazenjaDo;
	}

	public void setDatumVazenjaDo(Date datumVazenjaDo) {
		this.datumVazenjaDo = datumVazenjaDo;
	}

	public Date getDatumPrimenaOd() {
		return datumPrimenaOd;
	}

	public void setDatumPrimenaOd(Date datumPrimenaOd) {
		this.datumPrimenaOd = datumPrimenaOd;
	}

	public Date getDatumPrimenaDo() {
		return datumPrimenaDo;
	}

	public void setDatumPrimenaDo(Date datumPrimenaDo) {
		this.datumPrimenaDo = datumPrimenaDo;
	}

	public Date getDatumStupanjeOd() {
		return datumStupanjeOd;
	}

	public void setDatumStupanjeOd(Date datumStupanjeOd) {
		this.datumStupanjeOd = datumStupanjeOd;
	}

	public Date getDatumStupanjeDo() {
		return datumStupanjeDo;
	}

	public void setDatumStupanjeDo(Date datumStupanjeDo) {
		this.datumStupanjeDo = datumStupanjeDo;
	}

	public Date getDatumPredlogOd() {
		return datumPredlogOd;
	}

	public void setDatumPredlogOd(Date datumPredlogOd) {
		this.datumPredlogOd = datumPredlogOd;
	}

	public Date getDatumPredlogDo() {
		return datumPredlogDo;
	}

	public void setDatumPredlogDo(Date datumPredlogDo) {
		this.datumPredlogDo = datumPredlogDo;
	}
}
