/*
 *
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.recovery.password;

//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.osgi.service.component.annotations.Reference;
//import org.osgi.service.component.annotations.ReferenceCardinality;
//import org.osgi.service.component.annotations.ReferencePolicy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.mgt.User;
import org.wso2.carbon.identity.recovery.ChallengeQuestionManager;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.RecoveryScenarios;
import org.wso2.carbon.identity.recovery.RecoverySteps;
import org.wso2.carbon.identity.recovery.bean.ChallengeQuestionsResponse;
import org.wso2.carbon.identity.recovery.mapping.SecurityQuestionsConfig;
import org.wso2.carbon.identity.recovery.model.ChallengeQuestion;
import org.wso2.carbon.identity.recovery.model.UserChallengeAnswer;
import org.wso2.carbon.identity.recovery.model.UserRecoveryData;
import org.wso2.carbon.identity.recovery.store.JDBCRecoveryDataStore;
import org.wso2.carbon.identity.recovery.store.UserRecoveryDataStore;
import org.wso2.carbon.identity.recovery.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

//import org.wso2.carbon.base.MultitenantConstants;
//import org.wso2.carbon.identity.application.common.model.Property;
//import org.wso2.carbon.identity.application.common.model.User;
//import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
//import org.wso2.carbon.identity.core.util.IdentityUtil;
//import org.wso2.carbon.identity.event.IdentityEventConstants;
//import org.wso2.carbon.identity.event.IdentityEventException;
//import org.wso2.carbon.identity.event.event.Event;
//import org.wso2.carbon.identity.mgt.RealmService;
//import org.wso2.carbon.registry.core.utils.UUIDGenerator;
//import org.wso2.carbon.user.api.UserStoreException;
//import org.wso2.carbon.user.api.UserStoreManager;
//import org.wso2.carbon.user.core.UserCoreConstants;
//import org.wso2.carbon.user.core.UserRealm;
//import org.wso2.carbon.user.core.service.RealmService;


/**
 * Security Question Password Recovery Manager
 */
public class SecurityQuestionPasswordRecoveryManager {

    private static final Logger log = LoggerFactory.getLogger(SecurityQuestionPasswordRecoveryManager.class);

//    private static final String PROPERTY_ACCOUNT_LOCK_ON_FAILURE = "account.lock.handler.enable";
//
//    private static final String PROPERTY_ACCOUNT_LOCK_ON_FAILURE_MAX = "account.lock.handler.On.Failure.Max.Attempts";


    private static SecurityQuestionPasswordRecoveryManager instance = new SecurityQuestionPasswordRecoveryManager();

    private static SecurityQuestionsConfig securityQuestionsConfig;

    private SecurityQuestionPasswordRecoveryManager() {

    }

    public static SecurityQuestionPasswordRecoveryManager getInstance() {
        securityQuestionsConfig = new SecurityQuestionsConfig();
        return instance;
    }

    public ChallengeQuestionsResponse initiateUserChallengeQuestion(User user) throws IdentityRecoveryException {


//        boolean isNotificationInternallyManaged = Boolean.parseBoolean(Utils.getRecoveryConfigs
//                (IdentityRecoveryConstants.ConnectorConfig.NOTIFICATION_INTERNALLY_MANAGE, user.getTenantDomain()));
//      boolean isNotificationInternallyManaged = true;//TODO get from config bean

        UserRecoveryDataStore userRecoveryDataStore = JDBCRecoveryDataStore.getInstance();
        userRecoveryDataStore.invalidateByUserUniqueId(user.getUniqueUserId());

        String challengeQuestionSeparator = securityQuestionsConfig.getQuestionSeparator();

//        int tenantId = IdentityTenantUtil.getTenantId(user.getTenantDomain());
//        UserStoreManager userStoreManager;
//        try {
//            userStoreManager = IdentityRecoveryServiceDataHolder.getInstance().getRealmService().
//                    getTenantUserRealm(tenantId).getUserStoreManager();
//            String domainQualifiedUsername = IdentityUtil.addDomainToName(user.getUserName(),
// user.getUserStoreDomain());
//            if (!userStoreManager.isExistingUser(domainQualifiedUsername)) {
//                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_INVALID_USER,
//                        domainQualifiedUsername);
//            }
//
//        } catch (UserStoreException e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_UNEXPECTED, null);
//        }

        //TODO Check account lock and account disable
//        if (Utils.isAccountDisabled(user)) {
//            throw Utils.handleClientException(
//                    IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_DISABLED_ACCOUNT, user.getUserName());
//        } else if (Utils.isAccountLocked(user)) {
//            throw Utils.handleClientException(
//                    IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_LOCKED_ACCOUNT, user.getUserName());
//        }

//        boolean isNotificationSendWhenInitiatingPWRecovery= false; //TODO get from config bean

//        if (isNotificationInternallyManaged && isNotificationSendWhenInitiatingPWRecovery) {
//            try {
////                triggerNotification(user, IdentityRecoveryConstants.NOTIFICATION_TYPE_PASSWORD_RESET_INITIATE,
// null);
//            } catch (Exception e) {
//                log.warn("Error while sending password reset initiating notification to user :" + user
// .getUniqueUserId());
//            }
//        }

        int minNoOfQuestionsToAnswer = securityQuestionsConfig.getMinAnswers(); //TODO get from config bean

        ChallengeQuestionManager challengeQuestionManager = ChallengeQuestionManager.getInstance();
        String[] ids = challengeQuestionManager.getUserChallengeQuestionIds(user);
        //TODO change to list

        if (ids == null || ids.length == 0) {
            throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
                    .ERROR_CODE_CHALLENGE_QUESTION_NOT_FOUND, user.getUniqueUserId());
        }

        if (ids.length > minNoOfQuestionsToAnswer) {
            ids = getRandomQuestionIds(ids, minNoOfQuestionsToAnswer);
        }

        String metaData = null;

        for (int i = 0; i < ids.length; i++) {
            if (i == 0) {
                metaData = ids[0];
            } else {
                metaData = metaData + challengeQuestionSeparator + ids[i];
            }
        }

        ChallengeQuestion userChallengeQuestion = challengeQuestionManager.getUserChallengeQuestion(user
                .getUniqueUserId(), ids[0]);
        List<ChallengeQuestion> questions = new ArrayList<>();
        questions.add(userChallengeQuestion);
        ChallengeQuestionsResponse challengeQuestionsResponse = new ChallengeQuestionsResponse(questions);

        String secretKey = UUID.randomUUID().toString();
        UserRecoveryData recoveryData = new UserRecoveryData(user.getUniqueUserId(), secretKey, RecoveryScenarios
                .QUESTION_BASED_PW_RECOVERY, RecoverySteps.VALIDATE_CHALLENGE_QUESTION);
        recoveryData.setRemainingSetIds(metaData);

        challengeQuestionsResponse.setCode(secretKey);

        if (ids.length > 1) {
            challengeQuestionsResponse.setStatus(IdentityRecoveryConstants.RECOVERY_STATUS_INCOMPLETE);
        }

        userRecoveryDataStore.store(recoveryData);
        return challengeQuestionsResponse;
    }


    public ChallengeQuestionsResponse initiateUserChallengeQuestionAtOnce(User user) throws IdentityRecoveryException {
        String challengeQuestionSeparator = securityQuestionsConfig.getQuestionSeparator();
        String uniqueUserID = user.getUniqueUserId();

//        boolean isNotificationInternallyManaged = true; //TODO read from config bean


        UserRecoveryDataStore userRecoveryDataStore = JDBCRecoveryDataStore.getInstance();
        userRecoveryDataStore.invalidateByUserUniqueId(uniqueUserID);

        //TODO check account disable/lock
//        if (Utils.isAccountDisabled(user)) {
//            throw Utils.handleClientException(
//                    IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_DISABLED_ACCOUNT, null);
//        } else if (Utils.isAccountLocked(user)) {
//            throw Utils.handleClientException(
//                    IdentityRecoveryConstants.ErrorMessages.ERROR_CODE_LOCKED_ACCOUNT, null);
//        }

//        boolean isNotificationSendWhenInitiatingPWRecovery= false; //TODO read from config bean
//
//        if (isNotificationInternallyManaged && isNotificationSendWhenInitiatingPWRecovery) {
//            try {
////                triggerNotification(user, IdentityRecoveryConstants.NOTIFICATION_TYPE_PASSWORD_RESET_INITIATE,
// null);
//            } catch (Exception e) {
//                log.warn("Error while sending password reset initiating notification to user :" + user
// .getUniqueUserId());
//            }
//        }

        int minNoOfQuestionsToAnswer = securityQuestionsConfig.getMinAnswers(); //TODO read from config bean

        ChallengeQuestionManager challengeQuestionManager = ChallengeQuestionManager.getInstance();
        String[] ids = challengeQuestionManager.getUserChallengeQuestionIds(user);

        if (ids == null || ids.length == 0) {
//            throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                    .ERROR_CODE_CHALLENGE_QUESTION_NOT_FOUND, user.getUniqueUserId());
            return new ChallengeQuestionsResponse(new ArrayList<>());
        }


        if (ids.length > minNoOfQuestionsToAnswer) {
            ids = getRandomQuestionIds(ids, minNoOfQuestionsToAnswer);
        }

//        ChallengeQuestion questions[] = new ChallengeQuestion[ids.length];
        List<ChallengeQuestion> randomQuestions = new ArrayList<>();

        String allChallengeQuestions = null;
//        for (String id : ids) {
//            randomQuestions.add(challengeQuestionManager.getUserChallengeQuestion(user, id));
        for (int i = 0; i < ids.length; i++) {
            randomQuestions.add(challengeQuestionManager.getUserChallengeQuestion(user.getUniqueUserId(), ids[i]));
            if (i == 0) {
                allChallengeQuestions = ids[0];
            } else {
                allChallengeQuestions = allChallengeQuestions + challengeQuestionSeparator + ids[i];
            }
        }

//        }
//        ChallengeQuestionsResponse challengeQuestionResponse = new ChallengeQuestionsResponse(questions);
        ChallengeQuestionsResponse challengeQuestionResponse = new ChallengeQuestionsResponse(randomQuestions);

        String secretKey = UUID.randomUUID().toString();
        UserRecoveryData recoveryData = new UserRecoveryData(uniqueUserID, secretKey, RecoveryScenarios
                .QUESTION_BASED_PW_RECOVERY, RecoverySteps.VALIDATE_ALL_CHALLENGE_QUESTIONS);
        recoveryData.setRemainingSetIds(allChallengeQuestions);

        challengeQuestionResponse.setCode(secretKey);
        userRecoveryDataStore.store(recoveryData);
        return challengeQuestionResponse;
    }


    //TODO new method
//    public ChallengeQuestionResponse validateUserChallengeQuestionAnswers(User user,
//              List<UserChallengeAnswer> userChallengeAnswers, String code) throws IdentityRecoveryException {
//
//        UserRecoveryDataStore userRecoveryDataStore = JDBCRecoveryDataStore.getInstance();
//        UserRecoveryData userRecoveryData = userRecoveryDataStore.loadByCode(code);
//        //if return data from load, it means the code is validated. Otherwise it returns exceptions.
//
//        try {
//            if (userChallengeAnswers == null) {
//                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                        .ERROR_CODE_CHALLENGE_QUESTION_NOT_FOUND, null);
//            }
//
//            String challengeQuestionSeparator = securityQuestionsConfig.getQuestionSeparator();
//
//            if (RecoverySteps.VALIDATE_ALL_CHALLENGE_QUESTIONS.equals(userRecoveryData.getRecoveryStep())) {
//                String allChallengeQuestions = userRecoveryData.getRemainingSetIds();
//
//                if (StringUtils.isNotBlank(allChallengeQuestions)) {
//                    String[] requestedQuestions = allChallengeQuestions.split(challengeQuestionSeparator);
//
//                    if (requestedQuestions.length != userChallengeAnswers.size()) {
//                        throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                                .ERROR_CODE_NEED_TO_ANSWER_TO_REQUESTED_QUESTIONS, null);
//                    }
//                    validateQuestion(requestedQuestions, userChallengeAnswers);
//                    //Validate whether user answered all the requested questions
//
//                } else {
//                    throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                            .ERROR_CODE_CHALLENGE_QUESTION_NOT_FOUND, null);
//                }
//                ChallengeQuestionManager challengeQuestionManager = ChallengeQuestionManager.getInstance();
//
//                for (UserChallengeAnswer answer : userChallengeAnswers) {
//                    boolean verified = challengeQuestionManager.verifyUserChallengeAnswer(user, answer);
//                    if (!verified) {
////                    handleAnswerVerificationFail(userRecoveryData.getUser());
//                        throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                                .ERROR_CODE_INVALID_ANSWER_FOR_SECURITY_QUESTION, null);
//                    }
//                }
//
////                for (int i = 0; i < userChallengeAnswers.size(); i++) {
////                    boolean verified = challengeQuestionManager.verifyUserChallengeAnswer(user,
////                            userChallengeAnswer[i]);
////                    if (!verified) {
//////                    handleAnswerVerificationFail(userRecoveryData.getUser());
////                        throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
////                                .ERROR_CODE_INVALID_ANSWER_FOR_SECURITY_QUESTION, null);
////                    }
////                }
//
//                // Reset password recovery failed attempts
////                resetRecoveryPasswordFailedAttempts(userRecoveryData.getUser());
//
//                userRecoveryDataStore.invalidateByCode(code);
//                ChallengeQuestionResponse challengeQuestionResponse = new ChallengeQuestionResponse();
//                String secretKey = UUID.randomUUID().toString();
//                challengeQuestionResponse.setCode(secretKey);
//                challengeQuestionResponse.setStatus(IdentityRecoveryConstants.RECOVERY_STATUS_COMPLETE);
//                UserRecoveryData recoveryData = new UserRecoveryData(user.getUniqueUserId(), secretKey,
//                        RecoveryScenarios.QUESTION_BASED_PW_RECOVERY);
//
//                recoveryData.setRecoveryStep(RecoverySteps.UPDATE_PASSWORD);
//
//                userRecoveryDataStore.store(recoveryData);
//
//                return challengeQuestionResponse;
//            } else {
//                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                        .ERROR_CODE_INVALID_CODE, null);
//            }
//        } catch (IdentityRecoveryClientException e) {
//            //handleAnswerVerificationFail(user);
//            throw e;
//        }
//    }

    public ChallengeQuestionsResponse validateUserChallengeQuestions(List<UserChallengeAnswer> userChallengeAnswer,
                                                                     String code) throws
            IdentityRecoveryException {

        UserRecoveryDataStore userRecoveryDataStore = JDBCRecoveryDataStore.getInstance();
        UserRecoveryData userRecoveryData = null;

        //if return data from load, it means the code is validated. Otherwise it returns exceptions.
        List<ChallengeQuestion> questions = new ArrayList<>();
        ChallengeQuestionsResponse challengeQuestionResponse = new ChallengeQuestionsResponse(questions);

        String challengeQuestionSeparator = securityQuestionsConfig.getQuestionSeparator();

        if (StringUtils.isEmpty(challengeQuestionSeparator)) {
            challengeQuestionSeparator = IdentityRecoveryConstants.DEFAULT_CHALLENGE_QUESTION_SEPARATOR;
        }

        try {
            userRecoveryData = userRecoveryDataStore.loadByCode(code);
        } catch (IdentityRecoveryException e) {
            log.error("Error while loading recovery data with code " + code, e);
            String errorCode = !StringUtils.isEmpty(e.getErrorCode()) ? e.getErrorCode() : IdentityRecoveryConstants
                    .ErrorMessages
                    .ERROR_CODE_UNEXPECTED.getCode();
            challengeQuestionResponse.setCode(code);
            challengeQuestionResponse.setStatus(errorCode);
            return challengeQuestionResponse;
        }

        String secretKey = userRecoveryData.getSecret();
        challengeQuestionResponse.setCode(secretKey);

        ChallengeQuestionManager challengeQuestionManager = ChallengeQuestionManager.getInstance();


        try {
            if (userChallengeAnswer == null) {
                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
                        .ERROR_CODE_CHALLENGE_QUESTION_NOT_FOUND, null);
            }

            if (RecoverySteps.VALIDATE_CHALLENGE_QUESTION.equals(userRecoveryData.getRecoveryStep())) {

                if (userChallengeAnswer.size() > 1) {
                    throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
                            .ERROR_CODE_MULTIPLE_QUESTION_NOT_ALLOWED, null);
                }

                String[] remainingQuestionIds = userRecoveryData.getRemainingSetIds().split(challengeQuestionSeparator);
                questions.add(validateQuestion(remainingQuestionIds[0], userChallengeAnswer.get(0),
                        userRecoveryData.getUserUniqueId(),
                        challengeQuestionManager));
                challengeQuestionResponse.setQuestions(questions);

                boolean verified = challengeQuestionManager.verifyUserChallengeAnswer(userRecoveryData
                        .getUserUniqueId(), userChallengeAnswer.get(0));
                if (verified) {
                    userRecoveryDataStore.invalidateByCode(code);
                    secretKey = UUID.randomUUID().toString();
                    challengeQuestionResponse.setCode(secretKey);

                    UserRecoveryData recoveryData = new UserRecoveryData(userRecoveryData.getUserUniqueId(),
                            secretKey, RecoveryScenarios.QUESTION_BASED_PW_RECOVERY);

                    String remainingSetIds = userRecoveryData.getRemainingSetIds();

                    if (remainingQuestionIds.length > 1) {
                        for (int i = 1; i < remainingQuestionIds.length; i++) {
                            if (i == 1) {
                                remainingSetIds = remainingQuestionIds[1];
                            } else {
                                remainingSetIds = remainingSetIds + challengeQuestionSeparator +
                                        remainingQuestionIds[i];
                            }
                        }
                        ChallengeQuestion challengeQuestion = challengeQuestionManager.getUserChallengeQuestion
                                (userRecoveryData.getUserUniqueId(), remainingQuestionIds[1]);
                        questions.remove(0);
                        questions.add(challengeQuestion);
                        challengeQuestionResponse.setQuestions(questions);
                        recoveryData.setRecoveryStep(RecoverySteps.VALIDATE_CHALLENGE_QUESTION);
                        challengeQuestionResponse.setStatus(IdentityRecoveryConstants.RECOVERY_STATUS_INCOMPLETE);
                        recoveryData.setRemainingSetIds(remainingSetIds);
                    } else {
                        questions.remove(0);
                        recoveryData.setRemainingSetIds("");
                        recoveryData.setRecoveryStep(RecoverySteps.UPDATE_PASSWORD);
                        challengeQuestionResponse.setStatus(IdentityRecoveryConstants.RECOVERY_STATUS_COMPLETE);
                    }

                    userRecoveryDataStore.store(recoveryData);

                    //TODO Reset password recovery failed attempts
                    //resetRecoveryPasswordFailedAttempts(userRecoveryData.getUser());

                    return challengeQuestionResponse;
                } else {
                    challengeQuestionResponse.setStatus(IdentityRecoveryConstants.ErrorMessages
                            .ERROR_CODE_INVALID_ANSWER_FOR_SECURITY_QUESTION.getCode());
                    return challengeQuestionResponse;
                }

            } else if (RecoverySteps.VALIDATE_ALL_CHALLENGE_QUESTIONS.equals(userRecoveryData.getRecoveryStep())) {
                String allChallengeQuestions = userRecoveryData.getRemainingSetIds();

                if (StringUtils.isNotBlank(allChallengeQuestions)) {
                    String[] requestedQuestions = allChallengeQuestions.split(challengeQuestionSeparator);

                    if (requestedQuestions.length != userChallengeAnswer.size()) {
                        throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
                                .ERROR_CODE_NEED_TO_ANSWER_TO_REQUESTED_QUESTIONS, null);
                    }
//                    validateQuestion(requestedQuestions, userChallengeAnswer);
                    questions = validateQuestions(requestedQuestions, userChallengeAnswer,
                            userRecoveryData.getUserUniqueId(),
                            challengeQuestionManager);
                    challengeQuestionResponse.setQuestions(questions);
                    //Validate whether user answered all the requested questions

                } else {
                    throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
                            .ERROR_CODE_CHALLENGE_QUESTION_NOT_FOUND, null);
                }
                for (int i = 0; i < userChallengeAnswer.size(); i++) {
                    boolean verified = challengeQuestionManager.verifyUserChallengeAnswer(userRecoveryData
                            .getUserUniqueId(), userChallengeAnswer.get(i));
                    if (!verified) {
//                    handleAnswerVerificationFail(userRecoveryData.getUser());
                        challengeQuestionResponse.setStatus(IdentityRecoveryConstants.ErrorMessages
                                .ERROR_CODE_INVALID_ANSWER_FOR_SECURITY_QUESTION.getCode());
                        return challengeQuestionResponse;
//                        throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                                .ERROR_CODE_INVALID_ANSWER_FOR_SECURITY_QUESTION, null);
                    }
                }

                // Reset password recovery failed attempts
                //resetRecoveryPasswordFailedAttempts(userRecoveryData.getUser());

                userRecoveryDataStore.invalidateByCode(code);
//                ChallengeQuestionResponse challengeQuestionResponse = new ChallengeQuestionResponse();
                secretKey = UUID.randomUUID().toString();
                challengeQuestionResponse.setCode(secretKey);
                challengeQuestionResponse.setStatus(IdentityRecoveryConstants.RECOVERY_STATUS_COMPLETE);
                UserRecoveryData recoveryData = new UserRecoveryData(userRecoveryData.getUserUniqueId(), secretKey,
                        RecoveryScenarios.QUESTION_BASED_PW_RECOVERY);

                recoveryData.setRecoveryStep(RecoverySteps.UPDATE_PASSWORD);

                userRecoveryDataStore.store(recoveryData);

                return challengeQuestionResponse;
            } else {
                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
                        .ERROR_CODE_INVALID_CODE, null);
            }
        } catch (IdentityRecoveryClientException e) {
            //handleAnswerVerificationFail(userRecoveryData.getUser());
            throw e;
        }
    }

//    private void validateQuestion(String[] requestedQuestions, List<UserChallengeAnswer> userChallengeAnswers)
//            throws IdentityRecoveryException {
//        List<String> userChallengeIds = new ArrayList<>();
////        for (int i = 0; i < userChallengeAnswer.length; i++) {
////            userChallengeIds.add(userChallengeAnswer[i].getQuestion().getQuestionSetId().toLowerCase());
////        }
//        userChallengeIds.addAll(userChallengeAnswers.stream().map(answer -> answer.getQuestion().getQuestionSetId()
//                .toLowerCase()).collect(Collectors.toList()));
//
//        for (int i = 0; i < requestedQuestions.length; i++) {
//            if (!userChallengeIds.contains(StringUtils.lowerCase(requestedQuestions[i]))) {
//                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                        .ERROR_CODE_NEED_TO_ANSWER_TO_REQUESTED_QUESTIONS, null);
//            }
//        }
//    }

    private List<ChallengeQuestion> validateQuestions(String[] requestedQuestions,
                                                      List<UserChallengeAnswer> userChallengeAnswers,
                                                      String userUniqueID,
                                                      ChallengeQuestionManager challengeQuestionManager)
            throws IdentityRecoveryException {
        List<String> userChallengeIds = new ArrayList<>();
        List<ChallengeQuestion> questions = new ArrayList<>();
//        for (int i = 0; i < userChallengeAnswer.length; i++) {
//            userChallengeIds.add(userChallengeAnswer[i].getQuestion().getQuestionSetId().toLowerCase());
//        }
        userChallengeIds.addAll(userChallengeAnswers.stream().map(answer -> answer.getQuestion().getQuestionSetId()
                .toLowerCase()).collect(Collectors.toList()));

        for (int i = 0; i < requestedQuestions.length; i++) {
            if (!userChallengeIds.contains(StringUtils.lowerCase(requestedQuestions[i]))) {
                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
                        .ERROR_CODE_NEED_TO_ANSWER_TO_REQUESTED_QUESTIONS, null);
            } else {
                String q = challengeQuestionManager.getUserChallengeQuestion(userUniqueID,
                        requestedQuestions[i]).getQuestion();
                ChallengeQuestion question = new ChallengeQuestion(requestedQuestions[i], q);
                questions.add(question);
            }
        }
        return questions;
    }

    private ChallengeQuestion validateQuestion(String requestedQuestions,
                                               UserChallengeAnswer userChallengeAnswer,
                                               String userUniqueID, ChallengeQuestionManager challengeQuestionManager)
            throws IdentityRecoveryException {
        if (!requestedQuestions.equals(userChallengeAnswer.getQuestion().getQuestionSetId())) {
            throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
                    .ERROR_CODE_NEED_TO_ANSWER_TO_ASKED_SECURITY_QUESTION, null);
        } else {
            String q = challengeQuestionManager.getUserChallengeQuestion(userUniqueID,
                    userChallengeAnswer.getQuestion().getQuestionSetId()).getQuestion();
            return new ChallengeQuestion(userChallengeAnswer.getQuestion().getQuestionSetId(), q);
        }
    }

//    private void validateQuestion(String[] requestedQuestions, UserChallengeAnswer[] userChallengeAnswer)
//            throws IdentityRecoveryException {
//        List<String> userChallengeIds = new ArrayList<>();
//        for (int i = 0; i < userChallengeAnswer.length; i++) {
//            userChallengeIds.add(userChallengeAnswer[i].getQuestion().getQuestionSetId().toLowerCase());
//        }
//
//        for (int i = 0; i < requestedQuestions.length; i++) {
//            if (!userChallengeIds.contains(requestedQuestions[i].toLowerCase())) {
//                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                        .ERROR_CODE_NEED_TO_ANSWER_TO_REQUESTED_QUESTIONS, null);
//            }
//        }
//    }


    private static String[] getRandomQuestionIds(String[] allQuesitons, int minNoOfQuestionsToAnswser) {
        ArrayList remainingQuestions = new ArrayList(Arrays.asList(allQuesitons));
        ArrayList selectedQuestions = new ArrayList();

        for (int i = 0; i < minNoOfQuestionsToAnswser; i++) {
            int random = new Random().nextInt(remainingQuestions.size());
            selectedQuestions.add(i, remainingQuestions.get(random));
            remainingQuestions.remove(random);
        }
        return (String[]) selectedQuestions.toArray(new String[selectedQuestions.size()]);
    }

//    private void triggerNotification(User user, String type, String code) throws IdentityRecoveryException {
//
//        String eventName = IdentityEventConstants.Event.TRIGGER_NOTIFICATION;
//
//        HashMap<String, Object> properties = new HashMap<>();
//        properties.put(IdentityEventConstants.EventProperty.USER_NAME, user.getUserName());
//        properties.put(IdentityEventConstants.EventProperty.TENANT_DOMAIN, user.getTenantDomain());
//        properties.put(IdentityEventConstants.EventProperty.USER_STORE_DOMAIN, user.getUserStoreDomain());
//
//        if (StringUtils.isNotBlank(code)) {
//            properties.put(IdentityRecoveryConstants.CONFIRMATION_CODE, code);
//        }
//        properties.put(IdentityRecoveryConstants.TEMPLATE_TYPE, type);
//        Event identityMgtEvent = new Event(eventName, properties);
//        try {
//            IdentityRecoveryServiceDataHolder.getInstance().getIdentityEventService().handleEvent(identityMgtEvent);
//        } catch (IdentityEventException e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
// .ERROR_CODE_TRIGGER_NOTIFICATION, user
//                    .getUserName(), e);
//        }
//    }

//    private Property[] getConnectorConfigs(String tenantDomain) throws IdentityRecoveryException {
//
//        Property[] connectorConfigs;
//        try {
//            connectorConfigs = IdentityRecoveryServiceDataHolder.getInstance()
//                    .getIdentityGovernanceService()
//                    .getConfiguration(
//                            new String[]{PROPERTY_ACCOUNT_LOCK_ON_FAILURE, PROPERTY_ACCOUNT_LOCK_ON_FAILURE_MAX},
//                            tenantDomain);
//        } catch (Exception e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                    .ERROR_CODE_FAILED_TO_LOAD_GOV_CONFIGS, null, e);
//        }
//        return connectorConfigs;
//    }

//    private void resetRecoveryPasswordFailedAttempts(User user) throws IdentityRecoveryException {

//        Property[] connectorConfigs = getConnectorConfigs(user.getTenantDomain());
//
//        for (Property connectorConfig : connectorConfigs) {
//            if ((PROPERTY_ACCOUNT_LOCK_ON_FAILURE.equals(connectorConfig.getName())) &&
//                    !Boolean.parseBoolean(connectorConfig.getValue())) {
//                return;
//            }
//        }
//
//        int tenantId = IdentityTenantUtil.getTenantId(user.getTenantDomain());
//
//        RealmService realmService = IdentityRecoveryServiceDataHolder.getInstance().getRealmService();
//        UserRealm userRealm;
//        try {
//            userRealm = (UserRealm) realmService.getTenantUserRealm(tenantId);
//        } catch (UserStoreException e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                    .ERROR_CODE_FAILED_TO_LOAD_REALM_SERVICE, user.getTenantDomain(), e);
//        }
//
//        org.wso2.carbon.user.core.UserStoreManager userStoreManager;
//        try {
//            userStoreManager = userRealm.getUserStoreManager();
//        } catch (UserStoreException e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                    .ERROR_CODE_FAILED_TO_LOAD_USER_STORE_MANAGER, null, e);
//        }
//
//        Map<String, String> updatedClaims = new HashMap<>();
//        updatedClaims.put(IdentityRecoveryConstants.PASSWORD_RESET_FAIL_ATTEMPTS_CLAIM, "0");
//        try {
//            userStoreManager.setUserClaimValues(IdentityUtil.addDomainToName(user.getUserName(),
//                    user.getUserStoreDomain()), updatedClaims, UserCoreConstants.DEFAULT_PROFILE);
//        } catch (org.wso2.carbon.user.core.UserStoreException e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                    .ERROR_CODE_FAILED_TO_UPDATE_USER_CLAIMS, null, e);
//        }
//    }

//    private void handleAnswerVerificationFail(User user) throws IdentityRecoveryException {

//        Property[] connectorConfigs = getConnectorConfigs(user.getTenantDomain());
//
//        int maxAttempts = 0;
//        for (Property connectorConfig : connectorConfigs) {
//            if ((PROPERTY_ACCOUNT_LOCK_ON_FAILURE.equals(connectorConfig.getName())) &&
//                    !Boolean.parseBoolean(connectorConfig.getValue())) {
//                return;
//            } else if (PROPERTY_ACCOUNT_LOCK_ON_FAILURE_MAX.equals(connectorConfig.getName())
//                    && NumberUtils.isNumber(connectorConfig.getValue())) {
//                maxAttempts = Integer.parseInt(connectorConfig.getValue());
//            }
//        }
//
//        int tenantId = IdentityTenantUtil.getTenantId(user.getTenantDomain());
//
//        RealmService realmService = IdentityRecoveryServiceDataHolder.getInstance().getRealmService();
//        UserRealm userRealm;
//        try {
//            userRealm = (UserRealm) realmService.getTenantUserRealm(tenantId);
//        } catch (UserStoreException e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                    .ERROR_CODE_FAILED_TO_LOAD_REALM_SERVICE, user.getTenantDomain(), e);
//        }
//
//        org.wso2.carbon.user.core.UserStoreManager userStoreManager;
//        try {
//            userStoreManager = userRealm.getUserStoreManager();
//        } catch (UserStoreException e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                    .ERROR_CODE_FAILED_TO_LOAD_USER_STORE_MANAGER, null, e);
//        }
//
//        Map<String, String> claimValues = null;
//        try {
//            claimValues = userStoreManager.getUserClaimValues(IdentityUtil.addDomainToName(user.getUserName(),
//                            user.getUserStoreDomain()),
//                    new String[]{IdentityRecoveryConstants.ACCOUNT_LOCKED_CLAIM,
//                            IdentityRecoveryConstants.PASSWORD_RESET_FAIL_ATTEMPTS_CLAIM},
//                    UserCoreConstants.DEFAULT_PROFILE);
//        } catch (org.wso2.carbon.user.core.UserStoreException e) {
//            throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                    .ERROR_CODE_FAILED_TO_LOAD_USER_CLAIMS, null, e);
//        }
//
//        if (Boolean.parseBoolean(claimValues.get(IdentityRecoveryConstants.ACCOUNT_LOCKED_CLAIM))) {
//            return;
//        }
//
//        int currentAttempts = 0;
//        if (NumberUtils.isNumber(claimValues.get(IdentityRecoveryConstants.PASSWORD_RESET_FAIL_ATTEMPTS_CLAIM))) {
//            currentAttempts = Integer.parseInt(claimValues.get(IdentityRecoveryConstants
//                    .PASSWORD_RESET_FAIL_ATTEMPTS_CLAIM));
//        }
//
//        Map<String, String> updatedClaims = new HashMap<>();
//        if ((currentAttempts + 1) >= maxAttempts) {
//            updatedClaims.put(IdentityRecoveryConstants.ACCOUNT_LOCKED_CLAIM, Boolean.TRUE.toString());
//            updatedClaims.put(IdentityRecoveryConstants.PASSWORD_RESET_FAIL_ATTEMPTS_CLAIM, "0");
//            try {
//                userStoreManager.setUserClaimValues(IdentityUtil.addDomainToName(user.getUserName(),
//                        user.getUserStoreDomain()), updatedClaims, UserCoreConstants.DEFAULT_PROFILE);
//                throw Utils.handleClientException(IdentityRecoveryConstants.ErrorMessages
//                        .ERROR_CODE_LOCKED_ACCOUNT, IdentityUtil.addDomainToName(user.getUserName(),
//                        user.getUserStoreDomain()));
//            } catch (org.wso2.carbon.user.core.UserStoreException e) {
//                throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                        .ERROR_CODE_FAILED_TO_UPDATE_USER_CLAIMS, null, e);
//            }
//        } else {
//            updatedClaims.put(IdentityRecoveryConstants.PASSWORD_RESET_FAIL_ATTEMPTS_CLAIM,
//                    String.valueOf(currentAttempts + 1));
//            try {
//                userStoreManager.setUserClaimValues(IdentityUtil.addDomainToName(user.getUserName(),
//                        user.getUserStoreDomain()), updatedClaims, UserCoreConstants.DEFAULT_PROFILE);
//            } catch (org.wso2.carbon.user.core.UserStoreException e) {
//                throw Utils.handleServerException(IdentityRecoveryConstants.ErrorMessages
//                        .ERROR_CODE_FAILED_TO_UPDATE_USER_CLAIMS, null, e);
//            }
//        }
//    }
}
