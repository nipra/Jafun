<%@ attribute name="bgcolor" %>
<%@ attribute name="border" %>
<%@ attribute name="padding" %>
<%@ attribute name="margin" %>
<%@ attribute name="title" %>
<div style="
   background-color: ${bgcolor!=null?bgcolor:"transparent"};
   border: solid black ${border!=null?border:"0"}px;
   margin: ${margin!=null?margin:"5"}px;
   
">
<div style="
   background-color: #000;
   color: ${bgcolor!=null?bgcolor:"transparent"};
   margin: 0;
   padding: ${title!=null?4:0}px;
   font-family: sans-serif;
   font-weight: bold;
   text-align: left;
">
${title}
</div>
<div style="padding: ${padding!=null?padding:"5"}px;">
<jsp:doBody/>
</div>
</div>
