package com.homework.notebookstable.controller;

import com.homework.notebookstable.model.Notebook;
import com.homework.notebookstable.model.NotebookService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet(value = "/notebooks")
public class NotebooksServlet extends HttpServlet {

    private NotebookService notebookService;

    @Override
    public void init() {
        notebookService = new NotebookService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        for (;;) {
            String isConnecting = req.getParameter("connection");

            if (isConnecting != null) {
                if (isConnecting.equals("true")) {
                    notebookService.connect();
                    req.setAttribute("notebooks", notebookService.getAll());
                } else {
                    notebookService.disconnect();
                    req.setAttribute("isConnected", "false");
                }
            }

            if (notebookService.isConnected()) {
                req.setAttribute("isConnected", "true");
                req.setAttribute("table_fields", NotebookService.TABLE_FIELDS_TO_DISPLAY);
                req.setAttribute("covers", notebookService.getData("Cover"));
                req.setAttribute("countries", notebookService.getData("Country"));
                req.setAttribute("page_types", notebookService.getData("PageType"));
            }

            String editMod = req.getParameter("edit_mod");
            if (editMod != null && !editMod.equals("null")) {
                req.setAttribute("edit_mod", editMod);
                req.setAttribute("notebooks", notebookService.getAll());

                if (editMod.equals("update")) {
                    req.setAttribute("notebook_to_update", new Notebook(req.getParameter("notebook_string")));
                    break;
                }
            }

            if (req.getParameter("insert_btn") != null) {
                doOperation("insert", -1L, req);
                break;
            }

            if (req.getParameter("update_btn") != null) {
                long id = (Long.parseLong(req.getParameter("id")));
                doOperation("update", id, req);
                break;
            }

            String idToDelete = req.getParameter("delete_btn");
            if (idToDelete != null) {
                doOperation("delete", Long.parseLong(idToDelete), req);
                break;
            }

            if (req.getAttribute("notebooks") == null && req.getParameter("notebooks") != null) {
                req.setAttribute("notebooks", notebookService.getAll());
                break;
            }

            String selectedColumn = req.getParameter("column_name");
            if (selectedColumn != null) {
                String selectedFilter = req.getParameter("filter_selector");
                req.setAttribute("column_name", selectedColumn);

                switch (selectedFilter) {
                    case "column":
                        req.setAttribute("column_data", notebookService.getData(selectedColumn));
                        break;
                    case "notebooks_amount":
                        req.setAttribute("notebooks_amount_data", notebookService.getNotebooksAmount(selectedColumn));
                        break;
                    case "max":
                        req.setAttribute("max_notebooks_column", notebookService.getMaxNotebooksColumn(selectedColumn));
                        break;
                    case "min":
                        req.setAttribute("min_notebooks_column", notebookService.getMinNotebooksColumn(selectedColumn));
                        break;
                }
                break;
            }

            String[] selectedCovers = req.getParameterValues("cover");
            if (selectedCovers != null) {
                String filter = notebookService.getQueryFilter("Cover", selectedCovers);
                req.setAttribute("notebooks", notebookService.getAll(filter));
                break;
            }

            String[] selectedCountries = req.getParameterValues("country");
            if (selectedCountries != null) {
                String filter = notebookService.getQueryFilter("Country", selectedCountries);
                req.setAttribute("notebooks", notebookService.getAll(filter));
                break;
            }

            String[] selectedPageTypes = req.getParameterValues("pageType");
            if (selectedPageTypes != null) {
                String filter = notebookService.getQueryFilter("PageType", selectedPageTypes);
                req.setAttribute("notebooks", notebookService.getAll(filter));
                break;
            }

            String from = req.getParameter("page_amount_from");
            String to = req.getParameter("page_amount_to");
            if (from != null && to != null) {
                String filter = notebookService.getPageAmountFilter(from, to);
                List<Notebook> notebooks = notebookService.getAll(filter);

                if (notebooks.isEmpty()) {
                    req.setAttribute("search_info", "not found");
                } else {
                    req.setAttribute("notebooks", notebookService.getAll(filter));
                }
            }
            break;
        }

        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/index.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void doOperation(String operation, Long id, HttpServletRequest req) {
        if (operation.equals("delete")) {
            notebookService.delete(id);
        } else {
            Notebook notebook = new Notebook(
                    id,
                    req.getParameter("name_input"),
                    req.getParameter("brand_input"),
                    req.getParameter("country_input"),
                    req.getParameter("cover_input"),
                    req.getParameter("page_type_input"),
                    Integer.parseInt(req.getParameter("page_amount_input")));

            boolean isSuccess = false;

            if (notebookService.isAnyEmptyInput(
                    req.getParameter("name_input"),
                    req.getParameter("brand_input"),
                    req.getParameter("country_input"))) {
                req.setAttribute("edit_warning", "Input fields cannot be empty!");
            } else if (notebookService.isExist(notebook)) {
                req.setAttribute("edit_warning", "Such a notebook already exists!");
            } else {
                isSuccess = operation.equals("insert")
                        ? notebookService.create(notebook)
                        : notebookService.update(notebook);
            }
            req.setAttribute("is_edit_success", isSuccess ? "true" : "false");
            req.setAttribute("edit_status", operation.equals("insert") ? "added!" : "altered!");
        }
        req.setAttribute("notebooks", notebookService.getAll());
    }
}
