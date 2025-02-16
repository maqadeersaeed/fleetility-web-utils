package com.fleetility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fleetility.dto.ErrorResponse;
import com.fleetility.dto.MessageDTO;
import com.fleetility.dto.MessageDTO.MessageTypes;
import com.fleetility.dto.MessageDetails;
import com.fleetility.exception.FleetilityException;
import com.fleetility.exception.FleetilityValidationException;
import com.fleetility.exception.InvalidTokenException;
import com.fleetility.exception.InvalidUserException;
import com.fleetility.exception.ServiceException;
import com.fleetility.exception.ServiceExceptionXYZ;
import com.fleetility.exception.UnAuthorizedException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final MessageSource messageSource;
//	private final HttpServletRequest context;
//	private final JwtUtils jwtUtils;

	/**
	 * Handle Generic Exceptions (Uncaught Exceptions)  (500 Internal Server Error)
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {
		log.debug("handleGenericException: {}", e.getMessage());

		Optional<List<MessageDTO>> messageDTOs = getMessageDTOs(e);
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "", request,  messageDTOs.orElse(Collections.emptyList()));
	}
	
	/**
	 * Validation Exceptions (500 Internal Server Error)
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class, FleetilityValidationException.class })
	public ResponseEntity<ErrorResponse> handleValidationExption(Exception e, HttpServletRequest request) {
		log.debug("MethodArgumentNotValidException   " + e.getMessage());

		List<MessageDetails> errors = new ArrayList<>();
		
		if (e instanceof MethodArgumentNotValidException mae) {
			mae.getBindingResult().getFieldErrors().forEach(el -> {
				errors.add(new MessageDetails(el.getDefaultMessage(), MessageTypes.ERROR.value, el.getField(), "", ""));
			});
		}
		
		if (e instanceof FleetilityValidationException fve) {
			fve.getFieldErrors().forEach(fe -> {
				errors.add(new MessageDetails(fe.getDefaultMessage(), MessageTypes.ERROR.value, fe.getField(), "", ""));
			});
		}
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMainErrorCode("Validation Exception");
		errorResponse.setMessage("Validation Exception Message");
		errorResponse.setErrors(errors);
		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle Security Exceptions (403 Forbidden)
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ InvalidUserException.class, UsernameNotFoundException.class, InvalidTokenException.class, AccessDeniedException.class})
	public ResponseEntity<ErrorResponse> handleInvalidAccessException(Exception e, HttpServletRequest request) {
		log.debug("handleInvalidAccessException " + e.getMessage());
		
		Optional<List<MessageDTO>> messageDTOs = getMessageDTOs(e);
		
		List<MessageDTO> messages = messageDTOs.orElseGet(() -> {
			List<MessageDTO> defaultMessages = new ArrayList<>();
			
			String messageKey = "";
			if (e instanceof AccessDeniedException ae) {
				messageKey = "error.access.denied";
			} else {
				messageKey = "invalid.user";
			}
			
			defaultMessages.add(new MessageDTO(MessageTypes.ERROR.value, messageKey, null, "", ""));
			return defaultMessages;
		});
		
		return buildResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", "", request, messages);
	}
	
	/**
	 * Handle JWT Expired Exception (403 Forbidden with ExpiredJWTException as main error code in payload)
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ ExpiredJwtException.class })
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(Exception e, HttpServletRequest request) {
		log.debug("handleExpiredJwtException   " + e.getMessage());
		Optional<List<MessageDTO>> messageDTOs = getMessageDTOs(e);
		return buildResponse(HttpStatus.FORBIDDEN, "ExpiredJwtException", "FORBIDDEN ERROR ExpiredJwtException", request, Collections.emptyList());
	}
	
	/**
	 * 
	 * Handle Security Exceptions (403 Forbidden)
	 * 
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ ServiceException.class, ServiceExceptionXYZ.class, FleetilityException.class })
	public ResponseEntity<ErrorResponse> handle(Exception e, HttpServletRequest request) {
		log.debug("ServiceException  1 " + e.getMessage());
		
		// ex.printStackTrace();
				
		Optional<List<MessageDTO>> messageDTOs = getMessageDTOs(e);
		
		List<MessageDTO> messages = messageDTOs.orElseGet(() -> {
			List<MessageDTO> defaultMessages = new ArrayList<>();
			defaultMessages.add(new MessageDTO(MessageTypes.ERROR.value, "general.exception", null, "", ""));
			return defaultMessages;
		});

		return buildResponse(HttpStatus.BAD_REQUEST, null, null, request, messages);

	}

	/**
	 * Handle UnAuthorized Security Exceptions (403 Forbidden)
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ UnAuthorizedException.class})
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(Exception e, HttpServletRequest request) {
		Optional<List<MessageDTO>> messageDTOs = getMessageDTOs(e);
		return buildResponse(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", null, request, messageDTOs.orElse(Collections.emptyList()));
	}

	

	  @ExceptionHandler(ClientAbortException.class)
	  public void handleLockException(ClientAbortException exception, HttpServletRequest request) {
		log.debug("handleLockException 3 ClientAbortException " );
	    final String message = "ClientAbortException generated by request {} {} from remote address {} with X-FORWARDED-FOR {}";
	    final String headerXFF = request.getHeader("X-FORWARDED-FOR");
	    log.warn(message, request.getMethod(), request.getRequestURL(), request.getRemoteAddr(), headerXFF);
	  }

	// KELP LIKE THIS ::: TODO DO WE NEED TO HANDLE THIS ?
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

	private Optional<List<MessageDTO>> getMessageDTOs(Exception e) {
		// Handle FleetilityException separately
		Optional<List<MessageDTO>> messageDTOs = Optional.empty();
		if (e instanceof FleetilityException fleetilityException) {
			messageDTOs = fleetilityException.getErrorCodes();
		}
		return messageDTOs;
	}
	
	private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String mainErrorCode, String message,
			HttpServletRequest request, List<MessageDTO> messages) {
		Locale locale = getReqLocale(request);

		// If messages are empty or null, return an empty list of errors
		List<MessageDetails> errorDetails = (messages == null || messages.isEmpty()) ? Collections.emptyList()
				: messages.stream()
						.map(msg -> new MessageDetails(
								getMessageResource(msg.getMessageCode(), locale, msg.getParams()), msg.getMessageType(),
								msg.getFieldName(), msg.getLinkUrl(),
								StringUtils.isNotBlank(msg.getMessageCode()) ? msg.getMessageCode() : ""))
						.collect(Collectors.toList());

		// Create ErrorResponse
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMainErrorCode(mainErrorCode);
		errorResponse.setMessage(message);
		errorResponse.setStatus(status.value());
		errorResponse.setErrors(errorDetails);

		return new ResponseEntity<>(errorResponse, status);
	}
	
	
	private String getMessageResource(String errorCode, Locale locale, String[] params) {

		String message = messageSource.getMessage(errorCode, params, locale);

		// message =setParamterMessageResource(params, message);
		return message;
	}

	/**
	 * This method replace params in massage with values from DTO
	 * 
	 * @param params
	 * @param massage
	 * @return massage
	 */
	private String setParamterMessageResource(String[] params, String massage) {
		if (params != null)
			for (int i = 0; i < params.length; i++)
				massage = massage.replace("{" + i + "}", params[i]);

		return massage;
	}

	private Locale getReqLocale(HttpServletRequest request) {
		return request.getLocale() != null ? request.getLocale() : new Locale("en", "US");

	}
}