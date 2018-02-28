package com.remswork.project.alice.resource;

import com.remswork.project.alice.exception.GradingFactorException;
import com.remswork.project.alice.model.QuizResult;
import com.remswork.project.alice.model.support.Message;
import com.remswork.project.alice.resource.links.QuizResultResourceLinks;
import com.remswork.project.alice.service.impl.QuizServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Component
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Path("result")
public class QuizResultResource {

    @Autowired
    private QuizServiceImpl quizService;
    private long studentId;
    private UriInfo uriInfo;

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @GET
    public Response getQuizResult(@PathParam("quizId") long quizId) {
        try {
            QuizResultResourceLinks resultResourceLinks = new QuizResultResourceLinks(uriInfo);
            QuizResult result = quizService.getQuizResultByQuizAndStudentId(quizId, studentId);
            result.addLink(resultResourceLinks.self(result.getId()));
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (GradingFactorException e) {
            e.printStackTrace();
            Message message = new Message(404, "Not Found", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();
        }
    }

    @POST
    public Response addQuizResult(@QueryParam("score") int score, @PathParam("quizId") long quizId) {
        try {
            QuizResultResourceLinks resultResourceLinks = new QuizResultResourceLinks(uriInfo);
            QuizResult result = quizService.addQuizResult(score, quizId, studentId);
            result.addLink(resultResourceLinks.self(result.getId()));
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (GradingFactorException e) {
            e.printStackTrace();
            Message message = new Message(404, "Not Found", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();
        }
    }

    @PUT
    public Response updateQuizResult(@PathParam("quizId") long quizId, @QueryParam("score") int score) {
        try {
            QuizResultResourceLinks resultResourceLinks = new QuizResultResourceLinks(uriInfo);
            QuizResult result =
                    quizService.updateQuizResultByQuizAndStudentId(score, quizId, studentId);
            result.addLink(resultResourceLinks.self(result.getId()));
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (GradingFactorException e) {
            e.printStackTrace();
            Message message = new Message(400, "Bad Request", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();
        }
    }

    @DELETE
    public Response deleteQuizResult(@PathParam("quizId") long quizId) {
        try {
            QuizResultResourceLinks resultResourceLinks = new QuizResultResourceLinks(uriInfo);
            QuizResult result = quizService.deleteQuizResultByQuizAndStudentId(quizId, studentId);
            result.addLink(resultResourceLinks.self(result.getId()));
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (GradingFactorException e) {
            e.printStackTrace();
            Message message = new Message(400, "Bad Request", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        }
    }
}
