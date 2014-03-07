package cz.jakubmaly.schematronassert.svrl.model;

import java.util.*;

import javax.xml.bind.annotation.*;

import org.apache.commons.collections.*;

import com.google.common.collect.*;

@XmlRootElement(name = "schematron-output")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationOutput {
	@XmlElements({ @XmlElement(name = "text") })
	private List<Text> text;
	@XmlElements({ @XmlElement(name = "ns") })
	private List<Ns> ns;
	@XmlElements({ @XmlElement(name = "ns-prefix-in-attribute-values") })
	private List<NsPrefixInAttributeValues> nsPrefixInAttributeValues;
	@XmlElements({
			@XmlElement(name = "active-pattern", required = true, type = ActivePattern.class),
			@XmlElement(name = "fired-rule", required = true, type = FiredRule.class),
			@XmlElement(name = "failed-assert", required = true, type = FailedAssert.class),
			@XmlElement(name = "successful-report", required = true, type = SuccessfulReport.class)
	})
	private List<Object> reportContents;
	@XmlAttribute(name = "title")
	private String title;
	@XmlAttribute(name = "phase")
	private String phase;
	@XmlAttribute(name = "schemaVersion")
	private String schemaVersion;

	public List<FailedAssert> getFailures() {
		@SuppressWarnings("unchecked")
		Collection<FailedAssert> result = CollectionUtils.select(reportContents, new Predicate() {
			public boolean evaluate(Object object) {
				return object instanceof FailedAssert;
			}
		});
		return Lists.newArrayList(result);
	}

	public List<SuccessfulReport> getReports() {
		@SuppressWarnings("unchecked")
		Collection<SuccessfulReport> result = CollectionUtils.select(reportContents, new Predicate() {
			public boolean evaluate(Object object) {
				return object instanceof SuccessfulReport;
			}
		});
		return Lists.newArrayList(result);
	}

	public List<Text> getText() {
		return text;
	}

	public void setText(List<Text> text) {
		this.text = text;
	}

	public List<Ns> getNs() {
		return ns;
	}

	public void setNs(List<Ns> ns) {
		this.ns = ns;
	}

	public List<NsPrefixInAttributeValues> getNsPrefixInAttributeValues() {
		return nsPrefixInAttributeValues;
	}

	public void setNsPrefixInAttributeValues(List<NsPrefixInAttributeValues> nsPrefixInAttributeValues) {
		this.nsPrefixInAttributeValues = nsPrefixInAttributeValues;
	}

	public List<Object> getReportContents() {
		return reportContents;
	}

	public void setReportContents(List<Object> reportContents) {
		this.reportContents = reportContents;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getSchemaVersion() {
		return schemaVersion;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	@Override
	public String toString() {
		return String.format("[ValidationOutput{failures:%s, reports:%s}]", getFailures().size(), getReports().size());
	}
}
