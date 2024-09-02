<%@ page import="com.homework.notebookstable.model.Notebook" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Notebooks</title>
    <style><%@ include file="style.css" %></style>
</head>
<body>
    <%
        boolean isConnected = request.getAttribute("isConnected") != null
                && request.getAttribute("isConnected").equals("true");
    %>
    <div class="main_block">
        <div class="left_sidebar">
            <form class="form" action="${pageContext.request.contextPath}/notebooks" method="post">
                <% if (!isConnected) { %>
                <button class="left_sidebar_btn" type="submit" name="connection" value="true">Connect</button>
                <%}%>
                <% if (isConnected) { %>
                <button class="left_sidebar_btn" type="submit" name="connection" value="false">Disconnect</button>
                <button class="left_sidebar_btn" type="submit" name="notebooks">Notebooks</button>
                <button class="left_sidebar_btn" type="submit" name="edit_mod" value="insert">Add</button>
                <%}%>
            </form>

            <% if (isConnected) { %>
            <form class="form" action="${pageContext.request.contextPath}/notebooks" method="post">
                <p class="section_header">Select filter:</p>
                <select class="selector" name="filter_selector">
                    <option value="column">All</option>
                    <option value="notebooks_amount">Notebooks amount</option>
                    <option value="max">Max notebooks</option>
                    <option value="min">Min notebooks</option>
                </select>

                <p class="section_header">Select column:</p>
                <ol class="selection_list">
                    <% for (String columnName : (String[]) request.getAttribute("table_fields")) { %>
                    <li>
                        <input class="radio" type="radio" name="column_name" value="<%=columnName%>" required>
                        <span class="selection_name"><%=columnName%></span>
                    </li>
                    <%}%>
                </ol>

                <button class="apply_btn" type="submit">Apply</button>
            </form>

            <form class="form" action="${pageContext.request.contextPath}/notebooks" method="post">
                <p class="section_header">Select cover:</p>

                <ol class="selection_list">
                <% for (String coverName : (List<String>) request.getAttribute("covers")) { %>
                    <li>
                        <input class="checkbox" type="checkbox" name="cover" value="<%=coverName%>">
                        <span class="selection_name"><%=coverName%></span>
                    </li>
                <%}%>
                </ol>

                <button class="apply_btn" type="submit">Apply</button>
            </form>

            <form class="form" action="${pageContext.request.contextPath}/notebooks" method="post">
                <p class="section_header">Select country:</p>

                <ol class="selection_list">
                    <% for (String countryName : (List<String>) request.getAttribute("countries")) { %>
                        <li>
                            <input class="checkbox" type="checkbox" name="country" value="<%=countryName%>">
                            <span class="selection_name"><%=countryName%></span>
                        </li>
                    <%}%>
                </ol>

                <button class="apply_btn" type="submit">Apply</button>
            </form>

            <form class="form" action="${pageContext.request.contextPath}/notebooks" method="post">
                <p class="section_header">Select page type:</p>

                <ol class="selection_list">
                    <% for (String pageType : (List<String>) request.getAttribute("page_types")) { %>
                    <li>
                        <input class="checkbox" type="checkbox" name="pageType" value="<%=pageType%>">
                        <span class="selection_name"><%=pageType%></span>
                    </li>
                    <%}%>
                </ol>

                <button class="apply_btn" type="submit">Apply</button>
            </form>

            <form class="form" action="${pageContext.request.contextPath}/notebooks" method="post">
                <p class="section_header">Number of pages:</p>

                <div class="container">
                    <input class="number_input" type="number" name="page_amount_from" min="4" max="1000" required>
                    <span class="header"> - </span>
                    <input class="number_input" type="number" name="page_amount_to" min="5" max="1000" required>
                </div>

                <button class="apply_btn" type="submit">Apply</button>
            </form>
            <%}%>
        </div>

        <div class="notebooks_block">
            <%
                String editMod = (String) request.getAttribute("edit_mod");
                if (editMod != null) {
                    Notebook notebookToUpdate = (Notebook) request.getAttribute("notebook_to_update");
            %>
            <form action="${pageContext.request.contextPath}/notebooks" method="post">
                <div class="editing_block">
                    <input
                      name="edit_mod"
                      value="<%=editMod.equals("update") ? "null" : "insert"%>"
                      hidden
                      readonly
                    >
                    <input
                      name="id"
                      value="<%=editMod.equals("update") ? notebookToUpdate.getId() : null%>"
                      hidden
                      readonly
                    >
                    <div class="edit_input_names">
                        <span class="edit_input_name">Name</span>
                        <span class="edit_input_name">Brand</span>
                        <span class="edit_input_name">Country</span>
                    </div>
                    <div class="edit_inputs_container">
                        <input
                                class="edit_input"
                                type="text"
                                name="name_input"
                                value="<%=notebookToUpdate != null ? notebookToUpdate.getName() : ""%>"
                                placeholder="Name"
                                maxlength="20"
                                required
                        >
                        <input
                                class="edit_input"
                                type="text"
                                name="brand_input"
                                value="<%=notebookToUpdate != null ? notebookToUpdate.getBrand() : ""%>"
                                placeholder="Brand"
                                maxlength="50"
                                required
                        >
                        <input
                                class="edit_input"
                                type="text"
                                name="country_input"
                                value="<%=notebookToUpdate != null ? notebookToUpdate.getCountry() : ""%>"
                                placeholder="Country"
                                maxlength="50"
                                required
                        >
                    </div>
                    <div class="edit_input_names">
                        <span class="edit_input_name">Cover</span>
                        <span class="edit_input_name">Page type</span>
                        <span class="edit_input_name">Page amount</span>
                    </div>
                        <div class="edit_inputs_container">
                            <select class="edit_selection" name="cover_input">
                                <%
                                    for (String cover : (List<String>) request.getAttribute("covers")) {
                                        if (notebookToUpdate != null && notebookToUpdate.getCover().equals(cover)) {
                                %>
                                <option value="<%=cover%>" selected><%=cover%></option>
                                <%} else {%>
                                <option value="<%=cover%>"><%=cover%></option>
                                <%}%>
                                <%}%>
                            </select>
                            <select class="edit_selection" name="page_type_input">
                                <%
                                    for (String pageType : (List<String>) request.getAttribute("page_types")) {
                                        if (notebookToUpdate != null && notebookToUpdate.getPageType().equals(pageType)) {
                                %>
                                <option value="<%=pageType%>" selected><%=pageType%></option>
                                <%} else {%>
                                <option value="<%=pageType%>"><%=pageType%></option>
                                <%}%>
                                <%}%>
                            </select>
                            <input
                                    class="edit_input"
                                    type="number"
                                    name="page_amount_input"
                                    value="<%=notebookToUpdate != null ? notebookToUpdate.getPageAmount() : ""%>"
                                    min="4"
                                    required
                            >
                        </div>
                        <button
                          class="submit_btn"
                          name="<%=editMod.equals("insert") ? "insert_btn" : "update_btn"%>"
                          type="submit"
                        >
                            <%=editMod.equals("insert") ? "Add" : "Alter"%>
                        </button>
                </div>
            </form>
            <%}%>
            <%
                if (request.getAttribute("is_edit_success") != null) {
                    boolean isEditSuccess = (request.getAttribute("is_edit_success")).equals("true");
                    if (isEditSuccess) {
                        String editStatus = (String) request.getAttribute("edit_status");
            %>
                <p class="edit_info"><%="Notebook successfully " + editStatus%></p>
            <%
                } else {
                String editWarning = (String)request.getAttribute("edit_warning");
            %>
                <p class="edit_warning"><%=editWarning%></p>
            <%}%>
            <%}%>

            <%
                List<Notebook> notebooks = (List<Notebook>) request.getAttribute("notebooks");
                if (notebooks != null) {
            %>
                <table class="notebooks_table">
                    <tr>
                        <th>Name</th>
                        <th>Brand</th>
                        <th>Country</th>
                        <th>Cover</th>
                        <th>Page type</th>
                        <th>Page amount</th>
                        <th class="edit_btn_column" colspan="2">Editing</th>
                    </tr>
                    <% for (Notebook notebook : notebooks) { %>
                        <tr>
                            <td><%=notebook.getName()%></td>
                            <td><%=notebook.getBrand()%></td>
                            <td><%=notebook.getCountry()%></td>
                            <td><%=notebook.getCover()%></td>
                            <td><%=notebook.getPageType()%></td>
                            <td><%=notebook.getPageAmount()%></td>
                            <td class="edit_btn_column">
                                <form action="${pageContext.request.contextPath}/notebooks" method="post">
                                    <input name="notebook_string" value="<%=notebook.toString()%>" hidden readonly>
                                    <button class="edit_btn" type="submit" name="edit_mod" value="update">Alter</button>
                                </form>
                            </td>
                            <td class="edit_btn_column">
                                <form action="${pageContext.request.contextPath}/notebooks" method="post">
                                    <button class="edit_btn" type="submit" name="delete_btn" value="<%=notebook.getId()%>">Delete</button>
                                </form>
                            </td>
                        </tr>
                    <%}%>
                </table>
            <%} else if (request.getAttribute("search_info") != null){%>
                <p class="search_info">Nothing found!</p>
            <%}%>
            <%
                String columnName = (String)request.getAttribute("column_name");
                if (columnName != null) {
                    List<String> columnData = (List<String>) request.getAttribute("column_data");
                    if (columnData != null) {
            %>
                        <table class="notebooks_table">
                            <tr>
                                <th><%=columnName%></th>
                            </tr>
                            <% for (String data : columnData) { %>
                            <tr>
                                <td><%=data%></td>
                            </tr>
                            <%}%>
                        </table>
                    <%}%>
                <%
                    List<String> notebooksAmountData = (List<String>) request.getAttribute("notebooks_amount_data");
                    if (notebooksAmountData != null) {
                %>
                        <table class="notebooks_table">
                            <tr>
                                <th><%=columnName%></th>
                                <th>Notebooks amount</th>
                            </tr>
                            <%
                                for (String stroke : notebooksAmountData) {
                                    String[] array = stroke.split("!!!");
                            %>
                            <tr>
                                <td><%=array[0]%></td>
                                <td><%=array[1]%></td>
                            </tr>
                            <%}%>
                        </table>
                <%}%>
                <%
                    String max = (String) request.getAttribute("max_notebooks_column");
                    if (max != null) {
                %>
                        <p class="notebooks_info">
                            <%=String.format("%s with the largest number of notebooks:", columnName)%>
                            <br><br>
                            <%=max%>
                        </p>
                <%}%>
                <%
                    String min = (String) request.getAttribute("min_notebooks_column");
                    if (min != null) {
                %>
                        <p class="notebooks_info">
                            <%=String.format("%s with the least number of notebooks:", columnName)%>
                            <br><br>
                            <%=min%>
                        </p>
                <%}%>
            <%}%>
        </div>
    </div>
</body>
</html>