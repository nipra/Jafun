<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="record">
    <xsl:value-of select="sequence"/>
    <xsl:text>: </xsl:text>
    <xsl:value-of select="date"/>
    <xsl:text>: </xsl:text>
    <xsl:value-of select="message"/>
  </xsl:template>
</xsl:stylesheet>
