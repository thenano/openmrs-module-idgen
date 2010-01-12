/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.idgen.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openmrs.PatientIdentifierType;
import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.idgen.AutoGenerationOption;
import org.openmrs.module.idgen.IdentifierPool;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.module.idgen.IdgenConstants;
import org.openmrs.module.idgen.LogEntry;
import org.openmrs.module.idgen.PooledIdentifier;
import org.openmrs.module.idgen.processor.IdentifierSourceProcessor;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface for IdentifierSource Service Methods
 */
@Transactional
public interface IdentifierSourceService extends OpenmrsService {
	
	/**
	 * @param id the id to retrieve for the given type
	 * @return all IdentifierSource types that are supported
	 * @should return all supported IdentifierSource types
	 */
	@Transactional(readOnly = true)
	public List<Class<? extends IdentifierSource>> getIdentifierSourceTypes();

	/**
	 * @param id the id to retrieve for the given type
	 * @return the IdentifierSource that matches the given type and id
	 * @should return a saved sequential identifier generator
	 * @should return a saved remote identifier source
	 * @should return a saved identifier pool
	 */
	@Transactional(readOnly = true)
	public IdentifierSource getIdentifierSource(Integer id) throws APIException;
	
	/**
	 * @param includeRetired if true, also returns retired IdentifierSources
	 * @return all IdentifierSources
	 * @should return all identifier sources
	 */
	@Transactional(readOnly = true)
	public List<IdentifierSource> getAllIdentifierSources(boolean includeRetired) throws APIException;
	
	/**
	 * Returns all IdentifierSources by PatientIdentifierType
	 * @param includeRetired if true, also returns retired IdentifierSources
	 * @return all IdentifierSources by PatientIdentifierType
	 * @throws APIException
	 * @should return all identifier sources by type
	 */
	@Transactional(readOnly = true)
	public Map<PatientIdentifierType, List<IdentifierSource>> getIdentifierSourcesByType(boolean includeRetired) throws APIException;
	
	/**
	 * Persists a IdentifierSource, either as a save or update.
	 * @param identifierSource
	 * @return the IdentifierSource that was passed in
	 * @should save a sequential identifier generator for later retrieval
	 * @should save a rest identifier generator for later retrieval
	 * @should save an identifier pool for later retrieval
	 */
	@Transactional
	@Authorized( IdgenConstants.PRIV_MANAGE_IDENTIFIER_SOURCES )
	public IdentifierSource saveIdentifierSource(IdentifierSource identifierSource) throws APIException;
	
	/**
	 * Deletes a IdentifierSource from the database.
	 * @param identifierSource the IdentifierSource to purge
	 * @should delete an IdentifierSource from the system
	 */
	@Transactional
	@Authorized( IdgenConstants.PRIV_MANAGE_IDENTIFIER_SOURCES )
	public void purgeIdentifierSource(IdentifierSource identifierSource) throws APIException;
	
	/**
	 * Generates a Single Identifiers from the given source
	 * @throws APIException
	 */
	@Transactional
	@Authorized( OpenmrsConstants.PRIV_EDIT_PATIENT_IDENTIFIERS )
	public String generateIdentifier(IdentifierSource source, String comment) throws APIException;
	
	/**
	 * Generates a List of Identifiers from the given source in the given quantity
	 * @throws APIException
	 */
	@Transactional
	@Authorized( IdgenConstants.PRIV_GENERATE_BATCH_OF_IDENTIFIERS )
	public List<String> generateIdentifiers(IdentifierSource source, Integer batchSize, String comment) throws APIException;
	
	/**
	 * Returns an appropriate IdentifierSourceProcessor for the given IdentifierSource
	 * @param source
	 * @return
	 */
	@Transactional(readOnly = true)
	public IdentifierSourceProcessor getProcessor(IdentifierSource source);
	
	/**
	 * Registers a new Processor to handle a particular IdentifierSource
	 * @param type
	 * @param processorToRegister
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public void registerProcessor(Class<? extends IdentifierSource> type, IdentifierSourceProcessor processorToRegister) throws APIException;

	/**
	 * Returns available identifiers from a pool 
	 */
	@Transactional(readOnly=true)
	public List<PooledIdentifier> getAvailableIdentifiers(IdentifierPool pool, int quantity) throws APIException;
	
	/**
	 * Returns Pooled Identifiers for the given source, with the given status options
	 */
	@Transactional(readOnly=true)
	public int getQuantityInPool(IdentifierPool pool, boolean availableOnly, boolean usedOnly) throws APIException;
	
	/**
	 * Adds a List of Identifiers to the given pool
	 * @throws APIException
	 */
	@Transactional
	@Authorized( IdgenConstants.PRIV_UPLOAD_BATCH_OF_IDENTIFIERS )
	public void addIdentifiersToPool(IdentifierPool pool, List<String> identifiers) throws APIException;
	
	/**
	 * Adds a batch of Identifiers to the given pool, from the attached source
	 * @throws APIException
	 */
	@Transactional
	@Authorized( IdgenConstants.PRIV_UPLOAD_BATCH_OF_IDENTIFIERS )
	public void addIdentifiersToPool(IdentifierPool pool, Integer batchSize) throws APIException;
	
	/**
	 * @param id the id to retrieve for the given type
	 * @return the AutoGenerationOption that matches the given PatientIdentifierType
	 */
	@Transactional(readOnly = true)
	public AutoGenerationOption getAutoGenerationOption(PatientIdentifierType type) throws APIException;
	
	/**
	 * Persists a AutoGenerationOption, either as a save or update.
	 * @param option
	 * @return the AutoGenerationOption that was passed in
	 */
	@Transactional
	@Authorized( IdgenConstants.PRIV_MANAGE_AUTOGENERATION_OPTIONS )
	public AutoGenerationOption saveAutoGenerationOption(AutoGenerationOption option) throws APIException;
	
	/**
	 * Deletes a AutoGenerationOption from the database.
	 * @param option the AutoGenerationOption to purge
	 * @should delete an AutoGenerationOption from the system
	 */
	@Transactional
	@Authorized( IdgenConstants.PRIV_MANAGE_AUTOGENERATION_OPTIONS )
	public void purgeAutoGenerationOption(AutoGenerationOption option) throws APIException;
	
	/**
	 * Retrieves the Log Entries that match the supplied parameters.  All parameters are optional.
	 * The identifier and comment parameters do a "like" match, the date parameters ignore time
	 */
	@Transactional(readOnly=true)
	public List<LogEntry> getLogEntries(IdentifierSource source, Date fromDate, Date toDate, 
										String identifier, User generatedBy, String comment) throws APIException;
}