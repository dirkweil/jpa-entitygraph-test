package de.gedoplan.beantrial.entitygraph.entity;

import de.gedoplan.baselibs.persistence.entity.GeneratedIntegerIdEntity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.FIELD)
public class MailAddress extends GeneratedIntegerIdEntity
{
  private static final long serialVersionUID = 1L;

  private String            userId;
  private String            domain;

  public MailAddress(String userId, String domain)
  {
    this.userId = userId;
    this.domain = domain;
  }

  protected MailAddress()
  {
  }

  public String getUserId()
  {
    return this.userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public String getDomain()
  {
    return this.domain;
  }

  public void setDomain(String domain)
  {
    this.domain = domain;
  }
}
