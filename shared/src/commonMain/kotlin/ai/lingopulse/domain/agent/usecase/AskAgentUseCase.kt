package ai.lingopulse.domain.agent.usecase

import ai.lingopulse.data.agent.remote.cerebras.model.CerebrasMessage
import ai.lingopulse.domain.common.model.Message
import ai.lingopulse.domain.agent.model.CerebrasResponseMessage
import ai.lingopulse.domain.agent.repository.CerebrasRepository
import ai.lingopulse.domain.common.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AskAgentUseCase {
    operator fun invoke(
        messages: List<Message>?,
        promptMessage: Message,
    ): Flow<CerebrasResponseMessage>
}

class AskAgentUseCaseImpl constructor(
    private val cerebrasRepository: CerebrasRepository
) : AskAgentUseCase {

    private val supportedModels = listOf(
        "llama-4-scout-17b-16e-instruct",
        "llama3.1-8b",
        "llama-3.3-70b",
        "qwen-3-32b",
        "deepseek-r1-distill-llama-70b"
    )

    override operator fun invoke(
        messages: List<Message>?,
        promptMessage: Message,
    ): Flow<CerebrasResponseMessage> = flow {
        val cerebrasMessages = mutableListOf<CerebrasMessage>()
        messages?.forEach {
            if (it.role != Role.SYSTEM) {
                cerebrasMessages.add(CerebrasMessage(it.textContent, it.role.mappedRole()))
            }
        }
        cerebrasMessages.add(
            CerebrasMessage(
                promptMessage.textContent,
                promptMessage.role.mappedRole()
            )
        )

        // TODO: Handle Error
        val response = cerebrasRepository.getChatCompletion(
            messages = cerebrasMessages,
            model = "llama3.1-8b", // Default model with 8192 tokens
            maxTokens = 8192
        )

        // Extract the content from the Cerebras response
        val content = response.choices.firstOrNull()?.message?.content ?: ""

        // For now, create a simple response message
        // You can modify this structure based on your needs
        emit(
            CerebrasResponseMessage(
                agent = "cerebras",
                message = content,
                content = content
            )
        )
    }
}