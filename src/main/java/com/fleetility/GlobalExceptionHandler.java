package com.fleetility;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fleetility.dto.ErrorResponse;
import com.fleetility.dto.MessageDTO;
import com.fleetility.dto.MessageDTO.MessageTypes;
import com.fleetility.dto.MessageDetails;
import com.fleetility.exception.ExpiredSSOTokenException;
import com.fleetility.exception.FleetilityException;
import com.fleetility.exception.InvalidTokenException;
import com.fleetility.exception.InvalidUserException;
import com.fleetility.exception.ServiceException;
import com.fleetility.exception.ServiceExceptionXYZ;
import com.fleetility.exception.UnAuthorizedException;
import com.fleetility.exception.UnverifiedUserException;
import com.fleetility.exception.ValidationException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
//	@Autowired
//	private HttpServletRequest context;

//	@Autowired
//	JwtUtils jwtUtils;

//	@ExceptionHandler({ InvalidUserException.class, UsernameNotFoundException.class, InvalidTokenException.class })
//	public ResponseEntity<ErrorResponse> handleInvalidUserException(Exception e, WebRequest request) {
//		FleetilityException ex = null;
//		log.debug("handleInvalidUserException   " + e.getMessage());
//		if (e instanceof FleetilityException) {
//			ex = (FleetilityException) e;
//		}
//		ErrorResponse errorResponse = new ErrorResponse();
//		Optional<List<MessageDTO>> messageDTOs = ex.getErrorCodes();
//
//
//		Locale loc = getReqLocale(request);
//		if (messageDTOs == null) {
//			messageDTOs = Optional.of(new ArrayList());
//			MessageDTO err = new MessageDTO(MessageTypes.ERROR.value, "error.invalid.user", null, "", "");
//			messageDTOs.map(element -> {
//				element.add(err);
//				return element;
//			});
//		}
//		List<MessageDetails> errors = new ArrayList<MessageDetails>();
//		if (messageDTOs != null)
//			errors = messageDTOs.get().stream()
//					.map(element -> new MessageDetails(
//							getMessageResource(element.getMessageCode(), loc, element.getParams()),
//							element.getMessageType(), element.getFieldName(), element.getLinkUrl(),""))
//					.collect(Collectors.toList());
//
//		errorResponse.setMainErrorCode("FORBIDDEN");
//		errorResponse.setMessage("FORBIDDEN");
//		errorResponse.setErrors(errors);
//		errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.FORBIDDEN);
//	}
	
//	@ExceptionHandler({ ServiceException.class, ServiceExceptionXYZ.class, FleetilityException.class })
//	public ResponseEntity<ErrorResponse> handle(Exception e, WebRequest request) {
//		FleetilityException ex = null;
//		//UserDTO userDTO = jwtUtils.getUserInfoFromJwtToken(context);
//		log.debug("ServiceException  1 " + e.getMessage());
//		if (e instanceof FleetilityException) {
//			ex = (FleetilityException) e;
//		}
//		ex.printStackTrace();
//		ErrorResponse errorResponse = new ErrorResponse();
//		Optional<List<MessageDTO>> errorDTOs = ex.getErrorCodes();
//		List<MessageDetails> errors = new ArrayList<MessageDetails>();
//		Locale loc = getReqLocale(request);
//		if (errorDTOs != null)
//			errors = errorDTOs.get().stream()
//					.map(element -> new MessageDetails(
//							getMessageResource(element.getMessageCode(), loc,
//											 element.getParams()
//											),
//							element.getMessageType(), element.getFieldName(), element.getLinkUrl(),
//							element.getMessageCode()))
//					.collect(Collectors.toList());
//		if(errors.isEmpty())
//			errors.add( new MessageDetails(getMessageResource("general.exception", loc, null),
//					MessageTypes.ERROR.value, "", "",""));
//		errorResponse.setMainErrorCode("Server Error");
//		errorResponse.setMessage(ex.getExceptionMessage());
//		errorResponse.setErrors(errors);
//		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//		errorResponse.setExtraData("{}");
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
//
//	}

//	@ExceptionHandler({ UnAuthorizedException.class })
//	public ResponseEntity<ErrorResponse> handleUnauthorizedException(Exception e, WebRequest request) {
//		FleetilityException ex = null;
//		if (e instanceof FleetilityException) {
//			ex = (FleetilityException) e;
//		}
//		e.printStackTrace();
//		ErrorResponse errorResponse = new ErrorResponse();
//		Optional<List<MessageDTO>> errorDTOs = ex.getErrorCodes();
//		List<MessageDetails> errors = new ArrayList<MessageDetails>();
//		Locale loc = getReqLocale(request);
//		if (errorDTOs != null)
//			errors = errorDTOs.get().stream()
//					.map(element -> new MessageDetails(
//							getMessageResource(element.getMessageCode(), loc, element.getParams()),
//							element.getMessageType(), element.getFieldName(), element.getLinkUrl(),element.getMessageCode()))
//					.collect(Collectors.toList());
//
//		errorResponse.setMainErrorCode("UNAUTHORIZED>>");
//		errorResponse.setMessage("UNAUTHORIZED ERROR>>");
//		errorResponse.setErrors(errors);
//		errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
//	}

//	@ExceptionHandler({ UnverifiedUserException.class})
//	public ResponseEntity<ErrorResponse> handleUnverifiedUserException(Exception e, WebRequest request) {
//		FleetilityException ex = null;
//		log.debug("handleUnverifiedUserException   " + e.getMessage());
//		if (e instanceof FleetilityException) {
//			ex = (FleetilityException) e;
//		}
//		ErrorResponse errorResponse = new ErrorResponse();
//		Optional<List<MessageDTO>> messageDTOs = ex.getErrorCodes();
//
//		// log.debug("request getLocale " + request.getLocale());
//		// log.debug("request getLocale " + request.getHeaderNames());
//		Locale loc = getReqLocale(request);
//		if (messageDTOs == null) {
//			messageDTOs = Optional.of(new ArrayList());
//			MessageDTO err = new MessageDTO(MessageTypes.ERROR.value, "error.invalid.user", null, "", "");
//			messageDTOs.map(element -> {
//				element.add(err);
//				return element;
//			});
//		}
//		
//		List<MessageDetails> errors = new ArrayList<MessageDetails>();
//
//		if (messageDTOs != null)
//			errors = messageDTOs.get().stream()
//					.map(element -> new MessageDetails(
//							getMessageResource(element.getMessageCode(), loc,
//											 element.getParams()
//											),
//							element.getMessageType(), element.getFieldName(), element.getLinkUrl(),
//							element.getMessageCode()))
//					.collect(Collectors.toList());
//		errorResponse.setMainErrorCode("UPGRADE_REQUIRED>>");
//		errorResponse.setMessage("UPGRADE_REQUIRED ERROR>>");
//		errorResponse.setErrors(errors);
//		errorResponse.setStatus(HttpStatus.UPGRADE_REQUIRED.value());
//		errorResponse.setExtraData(ex.getExtraData());
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UPGRADE_REQUIRED);
//
//	}
	
//	@ExceptionHandler({ ExpiredJwtException.class })
//	public ResponseEntity<ErrorResponse> handleExpiredJwtException(Exception e, WebRequest request) {
//		log.debug("handleExpiredJwtException   " + e.getMessage());
//		ErrorResponse errorResponse = new ErrorResponse();
//		errorResponse.setMainErrorCode("ExpiredJwtException");
//		errorResponse.setMessage("FORBIDDEN ERROR ExpiredJwtException>>");
//		errorResponse.setErrors(null);
//		errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
//
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.FORBIDDEN);
//
//	}

//	@ExceptionHandler({ ExpiredSSOTokenException.class})
//	public ResponseEntity<ErrorResponse> handleExpiredSSOTokenException(Exception e, WebRequest request) {
//		FleetilityException ex = null;
//		log.debug("handleUnverifiedUserException   " + e.getMessage());
//		if (e instanceof FleetilityException) {
//			ex = (FleetilityException) e;
//		}
//		ErrorResponse errorResponse = new ErrorResponse();
//		Optional<List<MessageDTO>> messageDTOs = ex.getErrorCodes();
//
//		Locale loc = getReqLocale(request);
//		if (messageDTOs == null) {
//			messageDTOs = Optional.of(new ArrayList());
//			MessageDTO err = new MessageDTO(MessageTypes.ERROR.value, "error.invalid.user", null, "", "");
//			messageDTOs.map(element -> {
//				element.add(err);
//				return element;
//			});
//		}
//		
//		List<MessageDetails> errors = new ArrayList<MessageDetails>();
//
//		if (messageDTOs != null)
//			errors = messageDTOs.get().stream()
//					.map(element -> new MessageDetails(
//							getMessageResource(element.getMessageCode(), loc,
//											 element.getParams()
//											),
//							element.getMessageType(), element.getFieldName(), element.getLinkUrl(),
//							element.getMessageCode()))
//					.collect(Collectors.toList());
//		errorResponse.setMainErrorCode("UPGRADE_REQUIRED>>");
//		errorResponse.setMessage("UPGRADE_REQUIRED ERROR>>");
//		errorResponse.setErrors(errors);
//		errorResponse.setStatus(HttpStatus.UPGRADE_REQUIRED.value());
//		errorResponse.setExtraData(ex.getExtraData());
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UPGRADE_REQUIRED);
//
//	}
	
//	@ExceptionHandler({ MethodArgumentNotValidException.class })
//	public ResponseEntity<ErrorResponse> handleGeneralExption(MethodArgumentNotValidException e, WebRequest request) {
//
//		log.debug("MethodArgumentNotValidException   " + e.getMessage());
//		ErrorResponse errorResponse = new ErrorResponse();
//
//		List<MessageDetails> errors = new ArrayList<MessageDetails>();
//
//		e.getFieldErrors().stream().map(el -> el).forEach(el -> {
//			errors.add(new MessageDetails(el.getDefaultMessage(), MessageTypes.ERROR.value, el.getField(), "",""));
//
//		});
//		errorResponse.setMainErrorCode("Validation Exception");
//		errorResponse.setMessage("Validation Exception Message");
//		errorResponse.setErrors(errors);
//
//		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//
//	}

	/**
	 * 
	 * @param ValidationException
	 * @param request
	 * @return
	 */
//	@ExceptionHandler({ ValidationException.class })
//	public ResponseEntity<ErrorResponse> handleGeneralExption(ValidationException e, WebRequest request) {
//		log.debug("SEDDValidationException 3    " + e.getMessage());
//		ErrorResponse errorResponse = new ErrorResponse();
//		List<MessageDetails> errors = new ArrayList<MessageDetails>();
//		e.getFieldErrors().stream().forEach(el -> {
//			errors.add(new MessageDetails(el.getDefaultMessage(), MessageTypes.ERROR.value, el.getField(), "", ""));
//
//		});
//		errorResponse.setMainErrorCode("Validation Exception");
//		errorResponse.setMessage("Validation Exception Message");
//		errorResponse.setErrors(errors);
//		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	
//  @ExceptionHandler(ClientAbortException.class)
//  public void handleLockException(ClientAbortException exception, HttpServletRequest request) {
//	log.debug("handleLockException 3    <<<<<<<<<<<<<<<<<<<<<<<<" );
//    final String message = "ClientAbortException generated by request {} {} from remote address {} with X-FORWARDED-FOR {}";
//    final String headerXFF = request.getHeader("X-FORWARDED-FOR");
//    log.warn(message, request.getMethod(), request.getRequestURL(), request.getRemoteAddr(), headerXFF);
//  }
	
	// KELP LIKE THIS
//	@ExceptionHandler({ JpaSystemException.class ,Exception.class })
//	// @ResponseStatus(HttpStatus.BAD_REQUEST)
//	public ResponseEntity<ErrorResponse> handleGeneralExption(Exception e, WebRequest request) {
//		log.debug("Exception   " + e.getMessage());
//		log.error(e.getMessage(), e);
//		ErrorResponse errorResponse = new ErrorResponse();
//
//		// log.debug("request getLocale " + request.getLocale());
//		// log.debug("request getLocale " + request.getHeaderNames());
//		Locale loc = getReqLocale(request);
//		String mesDB = "Error";
//		try {
//			mesDB = getMessageResource("general.exception", loc, null);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		List<MessageDetails> errors = new ArrayList<MessageDetails>();
//		MessageDetails errorDetails = new MessageDetails(mesDB,
//				MessageTypes.ERROR.value, "", "","");
//		errors.add(errorDetails);
//		errorResponse.setMainErrorCode("Exception");
//		errorResponse.setMessage(e.getMessage());
//		errorResponse.setErrors(errors);
//		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//
//	}
	

	private String getMessageResource(String errorCode, Locale locale, String[] params) {

		String message = messageSource.getMessage(errorCode, params, locale);

		 //message =setParamterMessageResource(params, message);
		return message;
	}

	/**
	 * This method replace params in massage with values from DTO  
	 * @param params
	 * @param massage
	 * @return massage 
	 */
	private String setParamterMessageResource(String[] params, String massage) {
		if (params!= null)
			for (int i = 0; i < params.length; i++)
				massage = massage.replace("{" + i + "}", params[i]);

		return massage;
	}

	private Locale getReqLocale(WebRequest request) {
		return request.getLocale() != null ? request.getLocale() : new Locale("en", "US");

	}
}